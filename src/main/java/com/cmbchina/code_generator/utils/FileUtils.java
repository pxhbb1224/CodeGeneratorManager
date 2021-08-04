package com.cmbchina.code_generator.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * 文件工具类
 * @author Bin
 */

public class FileUtils {
    private static Logger log =LoggerFactory.getLogger(FileUtils.class);

    /**
     * 生成文件
     *
     * @param path
     *            路径
     * @param pathName
     *            文件名
     * @param text
     *            文件内容
     * @author John
     * @datatime 2017年9月12日上午1:30:32
     */
    public static void writeContent(String path, String pathName, String text)
    {
        createDir(path);
        File file = new File(path, pathName);
        FileWriter fw = null;
        BufferedWriter bw = null;

        try
        {
            boolean isCreate = file.createNewFile();
            if(!isCreate)
            {
                file.mkdir();
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
            bw.flush();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bw.close();
                fw.close();
            }
            catch(IOException io)
            {
                io.printStackTrace();
            }

        }
        return;
    }

    /**
     * 读取文件
     * @param pathName
     * @author John
     * @return
     */
    public static String readContent(String pathName)
    {
        try
        {
            File file = ResourceUtils.getFile(pathName);
            StringBuffer sb = new StringBuffer();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String str = null;
            while((str = br.readLine()) != null)
            {
                sb.append(str + "\n");
            }
            br.close();
            fr.close();
            return sb.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param destDirName
     * @return
     * @author John
     */
    public static boolean createDir(String destDirName)
    {
        File dir = new File(destDirName);
        if(dir.exists())
        {
            log.info("创建目录"+destDirName+"已存在！");
            return false;
        }
        if(!destDirName.endsWith(File.separator))
        {
            destDirName += File.separator;
        }
        if(dir.mkdirs())
        {
            System.out.println("创建目录"+destDirName+"成功！");
            return true;
        }
        else
        {
            System.out.println("创建目录"+destDirName+"失败！");
            return false;
        }
    }

    /**
     * 文件下载
     * @param uploadPath
     * @param filename
     * @param response
     * @throws IOException
     */
    public void downloadFile(String uploadPath, String filename, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String downloadFilename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
        OutputStream outputStream = response.getOutputStream();
        System.out.println(uploadPath + File.separator + filename);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(uploadPath + File.separator + filename)));
        byte[] buff = new byte[1024];
        System.out.println(bis);
        int readTmp;
        //int i = bis.read(buff);
        while ((readTmp = bis.read(buff)) > 0) {
            System.out.println(readTmp);
            outputStream.write(buff, 0, readTmp);//并不是每次都能读到1024个字节，所有用readTmp作为每次读取数据的长度，否则会出现文件损坏的错误
//            outputStream.flush();
//            i = bis.read(buff);
        }
        bis.close();
        outputStream.flush();
        outputStream.close();
    }
}
