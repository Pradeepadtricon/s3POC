package com.souro.file_upload.dao;

import com.souro.file_upload.exception.LMSDaoException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The type S 3 dao.
 */
@Repository
public class S3DaoImpl {
    /**
     * The Jdbc template.
     */
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Save file names int.
     *
     * @param courseId the course id
     * @param fileName the file name
     * @return the int
     * @throws LMSDaoException the lms dao exception
     */
    public int saveFileNames(String courseId, String fileName) throws LMSDaoException {
        List<String> data = getAllFileNames(courseId);
        try {
            if (data.contains(fileName)) {
                return 1;
            } else {
                return jdbcTemplate.update("INSERT INTO coursefile(courseId, fileName) VALUES(?,?)", courseId,
                    fileName);
            }
        }
        catch(Exception e){
            throw new LMSDaoException(e.getMessage());
        }
    }


    /**
     * Gets all file names.
     *
     * @param courseId the course id
     * @return the all file names
     * @throws LMSDaoException the lms dao exception
     */
    public List<String> getAllFileNames(String courseId) throws LMSDaoException {
        try {
            String query = "select fileName from coursefile where courseid=" + courseId;
            List<String> data = jdbcTemplate.queryForList(query, String.class);
            return data;
        }
        catch(Exception e){
            throw new LMSDaoException(e.getMessage());
        }
    }

    /**
     * Delete file names int.
     *
     * @param courseId the course id
     * @param fileName the file name
     * @return the int
     * @throws LMSDaoException the lms dao exception
     */
    public int deleteFileNames(String courseId, String fileName) throws LMSDaoException {
        try {
            String query = "delete from coursefile where courseId=? and fileName=?";
            return jdbcTemplate.update(query, new Object[] {courseId, fileName});
        }
        catch(Exception e){
            throw new LMSDaoException(e.getMessage());
        }

    }


}
