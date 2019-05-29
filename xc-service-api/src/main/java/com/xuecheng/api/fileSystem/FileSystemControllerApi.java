package com.xuecheng.api.fileSystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api("文件管理系统,提供文件上传")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件")
    public UploadFileResult uploadFile(MultipartFile multipartFile,
                                       String businesskey,
                                       String filetag,
                                       String metadata);

}
