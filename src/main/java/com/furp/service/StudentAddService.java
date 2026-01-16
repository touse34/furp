package com.furp.service;

import com.furp.DTO.StudentImportDTO;
import org.springframework.web.multipart.MultipartFile;

public interface StudentAddService {
    /**
     * 批量导入学生 (Excel)
     * @param file 前端上传的Excel文件
     */
    String batchImportStudents(MultipartFile file);

    /**
     * 手动录入单个学生
     * @param studentDTO 前端表单提交的学生信息
     */
    void addOneStudent(StudentImportDTO studentDTO);



}
