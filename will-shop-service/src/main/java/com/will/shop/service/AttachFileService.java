package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.model.AttachFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author will
 */
public interface AttachFileService extends IService<AttachFile> {

    /**
     * 上传文件到本地
     *
     * @param file
     * @return
     * @throws IOException e
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     */
    void deleteFile(String fileName);
}
