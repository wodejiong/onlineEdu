package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    //上传文件,返回FileId
    private String upload(MultipartFile multipartFile) {
        //初始化fastDfs
        initFastDfs();
        TrackerClient trackerClient=new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1=new StorageClient1(trackerServer,storageServer);
            String originalFilename = multipartFile.getOriginalFilename();
            byte[] bytes = multipartFile.getBytes();
            String ext = originalFilename.substring((originalFilename.lastIndexOf(".") + 1));
            String fileId = storageClient1.upload_file1(bytes, ext, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //初始化fastDfs参数
    private void initFastDfs() {
        try {
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.initByTrackers(tracker_servers);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.castException(FileSystemCode.FS_UPLOADFILE_INITFAIL);
        }

    }

    @Autowired
    FileSystemRepository fileSystemRepository;

    public UploadFileResult uploadFile(MultipartFile multipartFile,
                                       String businesskey,
                                       String filetag,
                                       String metadata){
        if (multipartFile == null) {
            ExceptionCast.castException(FileSystemCode.FS_DELETEFILE_NOTEXISTS);
        }

        //获取fileId
        String fileId = upload(multipartFile);

        FileSystem fileSystem=new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        if (StringUtils.isNotEmpty(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileType(multipartFile.getContentType());

        FileSystem save = fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, save);
    }
}
