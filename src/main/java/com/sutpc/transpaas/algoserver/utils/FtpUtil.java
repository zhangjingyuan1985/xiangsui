package com.sutpc.transpaas.algoserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FtpUtil {

  @Autowired FtpPool pool;

  /**
   * @param localPath 本地文件目录
   * @param workDirs
   * @param fileName
   * @throws IOException
   */
  public void upload(String localPath, String workDirs, String fileName) throws IOException {
    FTPClient ftpClient = pool.getFTPClient();
    InputStream input = null;
    File file = new File(localPath + File.separator + fileName);
    // 开始进行文件上传
    if (StringUtils.isBlank(fileName)) {
      fileName = file.getName();
    }
    try {
      input = new FileInputStream(file);
      String[] ret = new String[2];
      if (input.available() == 0) {
        log.error("文件大小为0");
      }
      ftpClient.setControlEncoding("UTF-8");
      String ftpFile = workDirs + File.separator + fileName;
      String remote = new String(ftpFile.getBytes("UTF-8"), "iso-8859-1");
      // 设置上传目录(没有则创建)
      if (!createDir(workDirs, ftpClient)) {
        log.error("切入FTP目录失败：" + workDirs);
      }
      // deleteFile(ftpClient, ftpFile);
      // 设置被动模式
      ftpClient.enterLocalPassiveMode();
      boolean result = ftpClient.storeFile(remote, input);
      if (!result) {
        log.error("上传文件失败");
      }
    } catch (Exception e) {
      log.error("上传失败", e);
    } finally {
      // 关闭资源
      input.close();
      // 归还资源
      pool.returnFTPClient(ftpClient);
    }
  }

  /**
   * 创建目录(有则切换目录，没有则创建目录)
   *
   * @param dir
   * @return
   */
  public boolean createDir(String dir, FTPClient ftpClient) {
    if (StringUtils.isBlank(dir)) {
      return true;
    }
    String path;
    try {
      // 目录编码，解决中文路径问题
      path = new String(dir.toString().getBytes("UTF-8"), "iso-8859-1");
      // 尝试切入目录
      if (ftpClient.changeWorkingDirectory(path)) {
        return true;
      }
      String[] arr = dir.split("/");
      StringBuffer sbfDir = new StringBuffer();
      // 循环生成子目录
      for (String s : arr) {
        sbfDir.append("/").append(s);
        // 目录编码，解决中文路径问题
        path = new String(sbfDir.toString().getBytes("UTF-8"), "iso-8859-1");
        // 尝试切入目录
        if (ftpClient.changeWorkingDirectory(path)) {
          continue;
        }
        if (!ftpClient.makeDirectory(path)) {
          log.info("[失败]ftp创建目录：" + sbfDir.toString());
          return false;
        }
      }
      // 将目录切换至指定路径
      return ftpClient.changeWorkingDirectory(path);
    } catch (Exception e) {
      log.error("FTP异常", e);
      return false;
    }
  }

  /**
   * 判断文件是否存在.
   *
   * @param ftpClient FTP连接
   * @param filePath 文件路径
   * @return boolean
   */
  public boolean isFtpFileExist(FTPClient ftpClient, String filePath) {
    boolean exists = false;
    try {
      final FTPFile[] files =
          ftpClient.listFiles(new String(filePath.getBytes("UTF-8"), "ISO-8859-1"));
      if (files != null && files.length > 0) {
        exists = true;
      }
    } catch (final IOException e) {
      log.error("failed to judge whether the file (" + filePath + ") is existed");
    }
    return exists;
  }

  /**
   * 删除FTP上的文件.
   *
   * @param ftpClient 连接对象
   * @param ftpPath 文件全路径
   * @return boolean
   */
  public boolean deleteFile(FTPClient ftpClient, String ftpPath) {
    if (ftpPath != null && ftpPath != "") {
      if (ftpPath.endsWith("/")) {
        log.info("错误的文件路径={}", ftpPath);
        return false;
      }
      try {
        final boolean exists = isFtpFileExist(ftpClient, ftpPath);
        if (exists) {
          ftpClient.deleteFile(new String(ftpPath.getBytes("UTF-8"), "ISO-8859-1"));
        } else {
          return true;
        }
      } catch (IOException e) {
        log.error("FTP上文件删除失败！", e);
        return false;
      }
    } else {
      log.info("没有需要删除的文件！");
    }
    return true;
  }

  /**
   * 文件下载
   *
   * @param remotePath
   * @param localPath
   * @param fileName
   * @return
   */
  public boolean downLoad(String remotePath, String localPath, String fileName) {
    boolean result = false;
    FTPClient ftpClient = pool.getFTPClient();
    try {
      // 转移到FTP服务器目录
      ftpClient.changeWorkingDirectory(remotePath);
      FTPFile[] files = ftpClient.listFiles();
      for (FTPFile file : files) {
        if (file.getName().equals(fileName)) {
          File localFileName = new File(localPath + File.separator + fileName);
          if (!localFileName.getParentFile().exists()) {
            localFileName.getParentFile().mkdirs();
          }
          if (localFileName.exists()) {
            localFileName.delete();
          }
          OutputStream is = new FileOutputStream(localFileName);
          ftpClient.retrieveFile(file.getName(), is);
          is.close();
        }
      }
      result = true;
    } catch (IOException e) {
      log.error("文件下载异常：", e);
    } finally {
      pool.returnFTPClient(ftpClient);
    }
    return result;
  }

  /**
   * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件， 如果要做指定文件移动，加个判断判断文件名
   * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName).
   *
   * @param oldPath 旧目录
   * @param newPath 新目录
   * @return
   */
  public boolean moveFile(String oldPath, String newPath) {
    boolean flag = false;
    FTPClient ftpClient = pool.getFTPClient();
    try {
      ftpClient.changeWorkingDirectory(oldPath);
      ftpClient.enterLocalPassiveMode();
      // 获取文件数组
      FTPFile[] files = ftpClient.listFiles();
      // 新文件夹不存在则创建
      if (!ftpClient.changeWorkingDirectory(newPath)) {
        ftpClient.makeDirectory(newPath);
      }
      // 回到原有工作目录
      ftpClient.changeWorkingDirectory(oldPath);
      for (FTPFile file : files) {
        // 转存目录
        flag =
            ftpClient.rename(
                new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"),
                    newPath + File.separator
                    + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
        if (flag) {
          log.info(file.getName() + "移动成功");
        } else {
          log.error(file.getName() + "移动失败");
        }
      }
    } catch (Exception e) {
      log.error("移动文件失败", e);
    } finally {
      pool.returnFTPClient(ftpClient);
    }
    return flag;
  }
}
