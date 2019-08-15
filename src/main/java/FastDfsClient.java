import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.csource.common.NameValuePair;

import java.io.IOException;
import java.net.URLDecoder;

public class FastDfsClient {
    private static TrackerServer trackerServer = null;
    private static TrackerClient trackerClient = null;
    private static StorageServer storageServer = null;
    private static StorageClient1 storageClient = null;
    private static String CFG_NAME = "/fast_dfs.conf";

    static {
        try {
            String confName = FastDfsClient.class.getResource(CFG_NAME).getPath();
            confName = URLDecoder.decode(confName, "utf-8");
            ClientGlobal.init(confName);
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageClient = new StorageClient1(trackerServer, storageServer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String uploadFile(String fileName, String ext, NameValuePair[] metas) throws Exception {
       try {
           return storageClient.upload_file1(fileName, ext, metas);
       } catch (Exception ex) {
           ex.printStackTrace();
       } finally {
           trackerServer.close();
           storageClient.close();
       }
        return null;
    }

    public static String uploadSlaveFile(String masterFileId, String fileName,String prefix, String ext, NameValuePair[] metas) throws Exception {
        try {
            return storageClient.upload_file1(masterFileId,prefix,fileName,ext,metas);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            trackerServer.close();
        }
        return null;
    }

    public static boolean deleteFile(String fileId) {
        try {
            return storageClient.delete_file1(fileId) == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            String fileId = uploadFile("/Users/jessica/Desktop/2.png", "png", null);
            System.out.println(fileId);
            String slaveFileId =uploadSlaveFile(fileId,"/Users/jessica/Desktop/2.png","_100x100", "png", null);
            System.out.println(slaveFileId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int download(String fileId, String localFile) throws Exception{
        int result = 0;
        //建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);

        //上传文件
        try {
            result = client.download_file1(fileId, localFile);
        } catch (Exception e) {
        }finally{
            trackerServer.close();
        }

        return result;
    }
}
