package com.sutpc.transpaas.algoserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sutpc.transpaas.algoserver.exception.BusinessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class CSVUtil {

  public static void main(String[] args) throws Exception {
    // 测试导出
    String filePath = "D:\\model_input\\SMALL_ZONE_WORK.csv";
    List<LinkedHashMap<String, String>> list = CSVUtil.readCSV(filePath);
    log.info(list.toString());
    String json = JSON.toJSON(list).toString();
    CSVUtil.createCSV(json, "D:\\model_input", "test.csv");
  }

  /**
   * csv文件读取：第一行默认为表头
   *
   * @param csvFile
   * @return
   * @throws IOException
   */
  public static List<LinkedHashMap<String, String>> readCSV(final String csvFile) {
    List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
    try {
      InputStream is = new FileInputStream(csvFile);
      InputStreamReader isr = new InputStreamReader(is, "UTF-8");
      Reader reader = new BufferedReader(isr);
      CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
      List<String> headerNames = parser.getHeaderNames();
      List<CSVRecord> csvRecords = parser.getRecords();
      LinkedHashMap<String, String> map;
      for (CSVRecord record : csvRecords) {
        map = new LinkedHashMap<>();
        for (String headerKey : headerNames) {
          map.put(headerKey, record.get(headerKey));
        }
        resultList.add(map);
      }
    } catch (NoSuchFileException e) {
      throw new BusinessException("404", csvFile + "文件不存在");
    } catch (Exception e) {
      log.error(csvFile +"文件读取异常", e);
    }
    return resultList;
  }

  /**
   * 写CSV文件
   *
   * @param content
   * @param filePath
   * @param fileName
   * @throws IOException
   */
  public static void createCSV(String content, String filePath, String fileName)
      throws IOException {
    String csvFile = filePath + File.separator + fileName;
    // 保证创建一个新文件
    File file = new File(csvFile);
    // 如果父目录不存在，创建父目录
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    // 如果已存在,删除旧文件
    if (file.exists()) {
      file.delete();
    }
    //生成list用以写入CSV
    List<LinkedHashMap> exportData = JSON.parseArray(content, LinkedHashMap.class);
    Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(csvFile), "UTF-8"));
    CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    try {
      //构建表头，即列名
      LinkedHashMap<String, Object> headMap = new LinkedHashMap<>();
      Iterator<?> headIterator = exportData.get(0).entrySet().iterator();
      List<String> record = new ArrayList<>();
      while (headIterator.hasNext()) {
        Map.Entry<String, Object> entry = (Map.Entry<String, Object>) headIterator.next();
        String key = entry.getKey();
        headMap.put(key, key);
        record.add(key);
      }
      printer.printRecord(record);
      List<List<String>> dataList = new ArrayList<>();
      for (LinkedHashMap<String, Object> linkedList : exportData) {
        Iterator<?> iterator = linkedList.entrySet().iterator();
        List<String> records = new ArrayList<>();
        while (iterator.hasNext()) {
          Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
          String value = entry.getValue()+"";
          records.add(value);
        }
        dataList.add(records);
      }
      printer.printRecords(dataList);
    } catch (IOException ex) {
      log.info("csv文件写入过程出行异常", ex);
    } finally {
      printer.close();
    }

  }

  /**
   * JSON转换为map
   */
  private static LinkedHashMap<String, String> json2Map(JSONObject jsonObject) {
    //转换为map对象
    LinkedHashMap<String, String> dateMap = new LinkedHashMap<>();
    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
      dateMap.put(entry.getKey(), (String) entry.getValue());
    }
    return dateMap;
  }


}
