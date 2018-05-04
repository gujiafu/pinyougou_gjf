package cn.itcast.fastdfs;

import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * @author GuJiaFu
 * @date 2018/5/4
 */
public class FastDFSTest {

    @Test
    public void dfstest() throws Exception {
        String conf_file = ClassLoader.getSystemResource("fastdfs/tracker.conf").getPath();
        ClientGlobal.init(conf_file);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        String[] uploadFile = storageClient.upload_file("D:\\image\\1.jpg", "jpg", null);

        if (uploadFile != null && uploadFile.length > 1) {
            for (String str : uploadFile) {
                System.out.println(str);
            }
            // 获取存储服务器信息
            String groupName = uploadFile[0];
            String filename = uploadFile[1];
            ServerInfo[] serverInfos =
                    trackerClient.getFetchStorages(trackerServer, groupName, filename);
            for (ServerInfo serverInfo : serverInfos) {
                System.out.println("ip=" + serverInfo.getIpAddr() + " ； port = " + serverInfo.getPort());
            }
            // 组合可以访问的路径
            String url = "http://" + serverInfos[0].getIpAddr() + "/" +
                    groupName + "/" + filename;
            System.out.println(url);


        }


    }
}