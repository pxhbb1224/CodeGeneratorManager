package com.cmbchina.code_generator.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.stream.FileImageInputStream;

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
}
