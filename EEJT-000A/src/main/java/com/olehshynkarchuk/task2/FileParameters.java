package com.olehshynkarchuk.task2;

import java.io.File;
import java.util.List;

public class FileParameters {

    private List<File> fileList;
    private String fileName;
    private String filenameExtensions;
    private long sizeStartRange;
    private long sizeEndRange;
    private long dateStartRange;
    private long dateEndRange;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilenameExtensions() {
        return filenameExtensions;
    }

    public void setFilenameExtensions(String filenameExtensions) {
        this.filenameExtensions = filenameExtensions;
    }

    public long getSizeStartRange() {
        return sizeStartRange;
    }

    public void setSizeStartRange(long sizeStartRange) {
        this.sizeStartRange = sizeStartRange;
    }

    public long getSizeEndRange() {
        return sizeEndRange;
    }

    public void setSizeEndRange(long sizeEndRange) {
        this.sizeEndRange = sizeEndRange;
    }

    public long getDateStartRange() {
        return dateStartRange;
    }

    public void setDateStartRange(long dateStartRange) {
        this.dateStartRange = dateStartRange;
    }

    public long getDateEndRange() {
        return dateEndRange;
    }

    public void setDateEndRange(long dateEndRange) {
        this.dateEndRange = dateEndRange;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

}
