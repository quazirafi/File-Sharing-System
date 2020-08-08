
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class ZipperClass2 {

    public static void addDirToZipArchive(ZipOutputStream zos, File fileToZip, String parrentDirectoryName) throws Exception {
        if (fileToZip == null || !fileToZip.exists()) {
            return;
        }

        String zipEntryName = fileToZip.getName();
        if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
            zipEntryName = parrentDirectoryName + "/" + fileToZip.getName();
        }

        if (fileToZip.isDirectory()) {
            System.out.println("+" + zipEntryName);
            for (File file : fileToZip.listFiles()) {
                addDirToZipArchive(zos, file, zipEntryName);
            }
        } else {
            System.out.println("   " + zipEntryName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(fileToZip);
            zos.putNextEntry(new ZipEntry(zipEntryName));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }

//    public static void main(String[] args) throws Exception {
//        FileOutputStream fos = new FileOutputStream("F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_9\\Server\\zipdemo.zip");
//        ZipOutputStream zos = new ZipOutputStream(fos);
//        addDirToZipArchive(zos, new File("F:\\Networking_Project\\File_Sharing_System-master\\Tester"), null);
//        zos.flush();
//        fos.flush();
//        zos.close();
//        fos.close();
//    }
    
    public static void goZip(String filePath,int count) throws Exception {
        //FileOutputStream fos = new FileOutputStream("F:\\Networking_Project\\File_Sharing_System-master\\FileSharingSystem3_9\\Server\\zipdemo.zip");
        FileOutputStream fos = new FileOutputStream(".\\download"+count+".zip");
        ZipOutputStream zos = new ZipOutputStream(fos);
        addDirToZipArchive(zos, new File(filePath), null);
        zos.flush();
        fos.flush();
        zos.close();
        fos.close();
    }
}
