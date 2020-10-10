package com.tiankong44.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @ClassName AvatarUtil
 * @Description TODO
 * @Author 12481
 * @Date 14:55
 * @Version 1.0
 **/
public class AvatarUtil {

    public static Image avatarAddText(String src, String text) throws IOException {
        // src = "G:\\Java\\IntelliJ_IDEA\\myblog\\src\\main\\resources\\static\\images\\avatar.jpg";
        File srcImgFile = new File(src);
        Image srcImg = ImageIO.read(srcImgFile);
        int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
        int srcImgHeight = srcImg.getHeight(null);//获取图片的高
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        g.setColor(Color.WHITE);
        text = text.substring(0, 1);
        Font font = null;
        if (isEnglish(text)) {
            font = new Font("宋体", Font.BOLD, 330);
            g.setFont(font);
            g.drawString(text, 60, 240);
            g.dispose();
        } else {
            font = new Font("宋体", Font.BOLD, 220);
            g.setFont(font);
            g.drawString(text, 40, 200);
            g.dispose();
        }
        FileOutputStream out = new FileOutputStream("E:\\ideaIC-2019.3.4.win\\MyProject\\myblog\\src\\main\\resources\\static\\images\\out.jpg");
        ImageIO.write(bufImg, "JPEG", out);
        out.close();
        return bufImg;
    }

    public static boolean isEnglish(String p) {
        byte[] bytes = p.getBytes();
        int i = bytes.length;//i为字节长度
        int j = p.length();//j为字符长度
        if (i == j) {
            return true;
        } else {
            return false;
        }
    }

    public static FileItem createFileItem(File file, String fieldName) {
        //DiskFileItemFactory()：构造一个配置好的该类的实例
        //第一个参数threshold(阈值)：以字节为单位.在该阈值之下的item会被存储在内存中，在该阈值之上的item会被当做文件存储
        //第二个参数data repository：将在其中创建文件的目录.用于配置在创建文件项目时，当文件项目大于临界值时使用的临时文件夹，默认采用系统默认的临时文件路径
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        //fieldName：表单字段的名称；第二个参数 ContentType；第三个参数isFormField；第四个：文件名
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(file);
            os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);//从buffer中得到数据进行写操作
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    public static void main(String[] args) throws IOException {
        String src = "/images/avatar.jpg";
        String text = "c";
        new AvatarUtil().avatarAddText(src, text);
    }
}
