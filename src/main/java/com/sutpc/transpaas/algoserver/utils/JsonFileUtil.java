package com.sutpc.transpaas.algoserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import lombok.extern.slf4j.Slf4j;

/** 生成json文件工具类 */
@Slf4j
public class JsonFileUtil {

  /**
   * 生成Json文件
   *
   * @param obj
   * @param fileName
   * @param filePath
   * @return
   */
  public static void createJsonFile(Object obj, String fileName, String filePath) throws Exception {

    boolean flag = true;
    String jsonString = obj.toString();
    // 拼接文件完整路径// 生成json格式文件
    String fullPath = filePath + File.separator + fileName;
    // 保证创建一个新文件
    File file = new File(fullPath);
    // 如果父目录不存在，创建父目录
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    // 如果已存在,删除旧文件
    if (file.exists()) {
      file.delete();
    }
    // 创建新文件
    file.createNewFile();

    if (jsonString.indexOf("'") != -1) {
      // 将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
      jsonString = jsonString.replaceAll("'", "\\'");
    }
    if (jsonString.indexOf("\"") != -1) {
      // 将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
      jsonString = jsonString.replaceAll("\"", "\\\"");
    }
    if (jsonString.indexOf("\r\n") != -1) {
      // 将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
      jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
    }
    if (jsonString.indexOf("\n") != -1) {
      // 将换行转换一下，因为JSON串中字符串不能出现显式的换行
      jsonString = jsonString.replaceAll("\n", "\\u000a");
    }
    // 将格式化后的字符串写入文件
    Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
    write.write(jsonString);
    write.flush();
    write.close();
  }

  /**
   * JSON文件读取.
   *
   * @param filePath 文件全路径
   * @return String
   */
  public static String readJsonFile(String filePath) {
    String jsonStr = "";
    try {
      File jsonFile = new File(filePath);
      FileReader fileReader = new FileReader(jsonFile);
      Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
      int ch = 0;
      StringBuffer sb = new StringBuffer();
      while ((ch = reader.read()) != -1) {
        sb.append((char) ch);
      }
      fileReader.close();
      reader.close();
      jsonStr = sb.toString();
      return jsonStr;
    } catch (IOException e) {
      log.error("JSON文件" + filePath + "读取异常：", e);
      return null;
    }
  }
}
