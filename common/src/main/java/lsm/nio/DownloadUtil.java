package lsm.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DownloadUtil {

    private String path; // 远程资源路径

    private String targetPath; // 本地存储路径

    private DownFileThread[] threads; // 线程list

    private int threadNum; // 线程数量

    private long totalSize; // 下载的文件大小

    // 构造初始化
    public DownloadUtil(String path, String targetPath, int threadNum) {
        this.path = path;
        this.targetPath = targetPath;
        this.threads = new DownFileThread[threadNum];
        this.threadNum = threadNum;
    }

    // 多线程下载文件资源
    public void download() {
        RandomAccessFile targetFile = null;
        try {
            URLConnection connection = new URL(path).openConnection();
            // 获取远程文件的大小
            totalSize = connection.getContentLength();

            // 设置本地文件大小
            targetFile = new RandomAccessFile(targetPath, "rw");
            targetFile.setLength(totalSize);

            // 每个线程下载大小
            long avgPart = totalSize / threadNum + 1;
            // 下载文件
            for (int i = 0; i < threadNum; i++) {
                long startPos = avgPart * i;
                RandomAccessFile targetTmp = new RandomAccessFile(targetPath,
                        "rw");
                targetTmp.seek(startPos); // 分段下载
                threads[i] = new DownFileThread(startPos, targetTmp, avgPart);
                threads[i].start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != targetFile){
                    targetFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 监控下载进度
    public double getDownRate() {
        int currentSize = 0;
        for (int i = 0; i < threadNum; i++) {
            currentSize += threads[i].length;
        }
        return currentSize * 1.0 / totalSize;
    }

    // 定义线程类
    class DownFileThread extends Thread {
        private long startPos;
        private RandomAccessFile raf;
        private long size;
        private long length;

        public DownFileThread(long startPos, RandomAccessFile raf, long size) {
            super();
            this.startPos = startPos;
            this.raf = raf;
            this.size = size;
        }

        public void run() {
            URL url;
            BufferedInputStream bufferedInputStream = null;
            try {
                url = new URL(path);
                URLConnection  conn = url.openConnection();
                conn.setReadTimeout(5 * 1000); // 设置超时时间为5秒
                //防止屏蔽程序抓取而返回403错误
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

                bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                bufferedInputStream.skip(this.startPos);
                byte[] buf = new byte[1024];
                int hasRead = 0;
                while (length < size && (hasRead = bufferedInputStream.read(buf)) != -1) {
                    raf.write(buf, 0, hasRead);
                    length += hasRead;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (null != bufferedInputStream)
                        bufferedInputStream.close();
                    if (null != raf)
                        raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 测试
    public static void main(String[] args) {
        String path = "http://mirror.bit.edu.cn/apache/tomcat/tomcat-8/v8.5.20/bin/apache-tomcat-8.5.20-windows-x64.zip";
        String targetPath = "D:/apache-tomcat-8.5.20.zip";
        long start = System.currentTimeMillis();
        final DownloadUtil download = new DownloadUtil(path, targetPath, 6);
        download.download();
        // 主线程负责下载文件，在启动一个线程负责监控下载的进度
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            while (download.getDownRate() < 1) {
                log.info("complete: {}", download.getDownRate());
                try {
                    Thread.sleep(200); // 200毫秒扫描一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("SPEND TIME:{}ms", System.currentTimeMillis() - start);
        });
    }
}
