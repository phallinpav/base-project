package com.sample.base_project.common.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public abstract class BaseOSSUtils implements Serializable {

//    public abstract <T extends OssService> T getOssService();
//
//    public boolean doesObjectExist(String path) {
//        try {
//            return getOssService().objectExist(path);
//        } catch (Exception e) {
//            throw ErrorMessageUtils.error("fail.to.check.file.exist", e);
//        }
//    }
//
//    public long getObjectSize(String path) {
//        try {
//            return getOssService().getObjectSize(path);
//        } catch (Exception e) {
//            throw ErrorMessageUtils.error("oss.file.get.fail", e);
//        }
//
//    }
//
//    public void updateObjectTagToDone(@Nullable String key) {
//        try {
//            if (org.apache.commons.lang3.StringUtils.isBlank(key)) {
//                return;
//            }
//
//            getOssService().setTag(key, "done", "1");
//        } catch (Exception e) {
//            throw ErrorMessageUtils.error(e.getMessage());
//        }
//    }
//    public void deleteObject(String path) {
//        try {
//            getOssService().deleteObject(path);
//        } catch (Exception e) {
//            log.error("Fail to delete oss object: ", e);
//        }
//    }
//
//    public void handleUpdateObject(String newPath, @Nullable String currentPath) {
//        if (newPath != null) {
//            if (StringUtils.isNotBlank(currentPath) && !newPath.equals(currentPath)) {
//                if (doesObjectExist(currentPath)) {
//                    deleteObject(currentPath);
//                }
//            }
//            updateObjectTagToDone(newPath);
//        }
//    }
}
