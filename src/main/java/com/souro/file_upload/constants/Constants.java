package com.souro.file_upload.constants;

/**
 * @author admin
 * <p>
 * This interface is used to define the constants used in the
 * application
 */
public interface Constants {

  //string constants
  public static final String SUCCESS = "Success";
  public static final String ERROR = "Error";
  public static final String ASSIGNMENT = "assignment";
  public static final String COURSE = "course";
  public static final String USER_ASSIGNMENT = "user_assignment";
  public static final String CSV = ".csv";
  public static final String TXT = ".txt";
  public static final int page = 1;
  public static final int offset = 5;
  public static final double fileSize = 4194304;
  //lms.support
  public static final String SUPPORT_EMAIL="lms.support@triconinfotech.com";

  //Email templates
  public static final String COURSE_CREATION_EMAIL_TEMPLATE = "CourseCreationEmail.vm";
  public static final String ACTIVATION_LINK_EMAIL_TEMPLATE = "ActivationEmail.vm";
  public static final String APPROVED_REQUEST_EMAIL_TEMPLATE = "ApprovedRequestEmail.vm";
  public static final String NOT_APPROVED_REQUEST_EMAIL_TEMPLATE = "NotApprovedRequestEmail.vm";
  public static final String ASSIGN_COURSE_EMAIL_TEMPLATE = "AssignCourseEmail.vm";
  public static final String UPDATE_USER_EMAIL_TEMPLATE = "UpdateUserEmail.vm";
  public static final String COURSE_PUBLISH_REJECT_EMAIL_TEMPLATE = "CoursePublishRequestRejectEmail.vm";
  public static final String COURSE_PUBLISH_APPROVE_EMAIL_TEMPLATE = "CoursePublishRequestApproveEmail.vm";
  public static final String AUTHOR_COURSE_APPROVAL_EMAIL_TEMPLATE = "AuthorCourseApprovalRequestEmail.vm";

  // SQL Queries


  public static final String INSERT_COMMENT = "insert into comments(parent_id,parent_table," +
      "comment,created_by) values (?,?,?,?)";

  // File
  public static final String INSERT_COURSE_FILE = "insert into coursefile(filename,file,courseId) values (?,?,?)";
  public static final String UPDATE_COURSE_FILE = "UPDATE coursefile SET filename=?," +
      "file=? where courseId=?";
  public static final String INSERT_ASSIGNMENT_FILE = "insert into assignmentfile(filename,file,courseId) values (?,?,?)";
  public static final String UPDATE_ASSIGNMENT_FILE = "UPDATE assignmentfile SET filename=?," +
      "file=? where courseId=?";
  public static final String INSERT_USER_ASSIGNMENT_FILE = "insert into user_assignment_ref" +
      "(user_email, assignment_id, attempts) values (?,?,?)";
  //email,id,machineScore,
  //						instructorScore,attempts,
  //						inputStream

  public static final String UPDATE_USER_ASSIGNMENT_FILE = "UPDATE user_assignment_ref set " +
      "machine_score=?,instructor_score=?,user_response_content=? where user_email=? AND " +
      "assignment_id=? and attempts=?";
  public static final String UPDATE_USER_ASSIGNMENT_FILE_DURING_ASSESSMENT = "UPDATE " +
      "user_assignment_ref set user_response_content=?, updated_on=? where user_email=? AND " +
      "assignment_id=? and attempts=?";
  public static final String FETCH_USER_ASSIGNMENT_FILE = "SELECT * FROM user_assignment_ref " +
      "WHERE user_email=? AND assignment_id=?";
  //FETCH_USER_ASSIGNMENT_FILE_FROM_EMAIL_COURSE_ID
  public static final String FETCH_USER_ASSIGNMENT_FILE_FROM_EMAIL_COURSE_ID =
      "SELECT ua.*,u.name,uc.userCourseStatus FROM user_assignment_ref as ua,user as u,user_course_ref as uc WHERE ua" +
          ".user_email IN (:emails) AND ua.user_email=u.email AND uc.email = u.email AND ua.assignment_id IN (SELECT " +
          "assignmentId from assignmentfile WHERE courseId = :courseId AND courseId = uc.courseId) ";
  public static final String FETCH_USER_ASSIGNMENT_FILE_FOR_ALL_BY_COURSE_ID =
      "SELECT ua.*,u.name,uc.userCourseStatus FROM user_assignment_ref as ua,user as u," +
          "user_course_ref as uc WHERE ua.user_email=u.email AND uc.email = u.email " +
          "AND ua.assignment_id IN (SELECT assignmentId from assignmentfile WHERE courseId = ? AND courseId = uc.courseId) ";
  public static final String FETCH_USER_ASSIGNMENT_FILE_FOR_ALL_BY_COURSE_ID_AND_ATTEMPT =
      "SELECT ua.*,u.name,uc.userCourseStatus FROM user_assignment_ref as ua,user as u,user_course_ref as uc WHERE ua.user_email = ? AND" +
          " ua.attempts = ? AND ua.user_email=u.email AND uc.email = u.email AND ua.assignment_id IN (SELECT " +
          "assignmentId from " +
          "assignmentfile WHERE courseId = ? AND courseId = uc.courseId) ";
  public static final String HARD_DELETE_COURSE_FILE = "delete from coursefile where courseId=?";
  public static final String HARD_DELETE_ASSIGNMENT_FILE = "delete from assignmentfile where " +
      "courseId=?";
  public static final String FETCH_COURSE_FILE = "select file,filename from coursefile WHERE courseId = ?";
  public static final String FETCH_ASSIGNMENT_FILE = "select file,filename,assignmentId from " +
      "assignmentfile WHERE courseId = ?";

  // Access Management
  public static final String FETCH_ENTITLEMENT_ID = "SELECT entitlementId FROM role_entitlement WHERE role IN (SELECT role FROM user  WHERE email=?)";
  public static final String FETCH_ENTITLEMENT_NAME = "SELECT entitlementName FROM entitlement WHERE entitlementId=?";
  public static final String FETCH_ENTITLEMENT_ID_FOR_NAME = "SELECT entitlementId From entitlement where entitlementName= ? ";


  // USER_COURSE
  public static final String REGISTER_USER_COURSE = "INSERT INTO user_course_ref(email,courseId," +
      "courseName,createdBy,durationValue,status,registeredOn,userCourseStatus) VALUES (?,?,?,?," +
      "?,?,?,?)";
  public static final String FETCH_REQUESTED_USER_COURSE = "SELECT uc.*,u.userImage FROM user_course_ref as uc,user as u WHERE uc.status LIKE ? AND uc.email=u.email order by registeredOn desc limit ?,? ";
  public static final String FETCH_REQUESTED_USER_COURSES_BY_SEARCH = "SELECT uc.*,u.userImage " +
      "FROM user_course_ref as uc,user as u WHERE uc.courseName like ? and uc.status LIKE ? AND " +
      "uc.email=u.email order by registeredOn desc limit ?,? ";
  public static final String FETCH_REQUESTED_COURSEID_USER_COURSE = "SELECT courseId,status," +
      "userCourseStatus" +
      " FROM user_course_ref WHERE email LIKE ? and (status like 'APPROVED' or status like 'PENDING')";
  public static final String UPDATE_USER_COURSE = "UPDATE user_course_ref SET status=?," +
      "registeredOn=?,userCourseStatus=? WHERE id=?";
  public static final String UPDATE_USER_COURSE_STATUS = "UPDATE user_course_ref SET userCourseStatus=? WHERE id=?";
  public static final String UPDATE_USER_COURSE_STATUS_WITH_EMAIL_COURSEID = "UPDATE " +
      "user_course_ref SET " +
      "userCourseStatus=? WHERE email=? AND courseId = ?";
  public static final String FETCH_ALL_APPROVED_COURSES = "SELECT uc.*,u.userImage,u.email as instructor_email,a.description," +
      "a.start_time,a.end_time,a.remainder,a.duration,a.attempts,a.is_full_screen_mode,a" +
      ".is_randomize_questions,a.show_answers,a.view_reports,a.toggles" +
      " FROM user_course_ref as uc,user as u,course as c,assessment_policy as a WHERE uc.status LIKE " +
      "'APPROVED' AND uc.courseId=c.courseId AND c.instructorEmail=u.email AND uc.email=? AND a" +
      ".course_id = c.courseId order by registeredOn desc limit ?,?";
  public static final String FETCH_ALL_APPROVED_COURSES_BY_SEARCH = "SELECT uc.*,u.userImage,a" +
      ".description,u.email as instructor_email," +
      "a.start_time,a.end_time,a.remainder,a.duration,a.attempts,a.is_full_screen_mode,a" +
      ".is_randomize_questions,a.show_answers,a.view_reports,a.toggles" +
      " FROM user_course_ref as uc,user as u,course as c,assessment_policy as a WHERE uc.status LIKE " +
      "'APPROVED' AND uc.courseName like ? and uc.courseId=c.courseId AND c.instructorEmail=u" +
      ".email AND uc.email=? AND a" +
      ".course_id = c.courseId order by registeredOn desc limit ?,?";
  public static final String FETCH_REGISTERED_COURSE_BY_COURSE_ID_AND_EMAIL = "SELECT uc.*,u" +
      ".userImage,u.email as instructor_email,a.description," +
      "a.start_time,a.end_time,a.remainder,a.duration,a.attempts,a.is_full_screen_mode,a" +
      ".is_randomize_questions,a.show_answers,a.view_reports,a.toggles" +
      " FROM user_course_ref as uc,user as u,course as c,assessment_policy as a WHERE uc.status LIKE " +
      "'APPROVED' AND uc.email=? AND uc.courseId = ? AND c.instructorEmail=u.email AND uc" +
      ".courseId=c.courseId AND a" +
      ".course_id = c.courseId";
  public static final String FETCH_ALL_USER_COURSES = "SELECT uc.*,u.userImage,a.description,a" +
      ".start_time,a.end_time,a.remainder,a.duration,a.attempts,a.is_full_screen_mode,a.is_randomize_questions," +
      "a.show_answers,a.view_reports FROM user_course_ref as uc,user as u,course as c," +
      "assessment_policy as a WHERE uc.courseId=c.courseId AND c.instructorEmail=u.email " +
      "AND uc.email=? AND a.course_id = c.courseId AND " +
      "uc.userCourseStatus = (SELECT userCourseStatus from user_course_ref where email = uc.email" +
      " group by userCourseStatus having count(*)>0 limit 1)" +
      " order by registeredOn desc limit ?,?";
  public static final String FETCH_ALL_USER_COURSES_FOR_GIVEN_STATUS = "SELECT uc.*,u.userImage,a" +
      ".description,a" +
      ".start_time,a.end_time,a.remainder,a.duration,a.attempts,a.is_full_screen_mode,a.is_randomize_questions," +
      "a.show_answers ,a.view_reports FROM user_course_ref as uc,user as u,course as c," +
      "assessment_policy as a WHERE uc.courseId=c.courseId AND c.instructorEmail=u.email " +
      "AND uc.email=? AND a.course_id = c.courseId AND " +
      "uc.userCourseStatus = ? order by registeredOn desc limit ?,?";
  public static final String DELETE_USER_COURSE = "DELETE FROM user_course_ref WHERE email =? AND courseId=?";//// used
  //// when
  //// transaction
  //// fails


  // USER
  public static final String FETCH_USER = " select * from user WHERE isVerified=true order by updatedOn desc";
  public static final String FETCH_ONE_USER = "SELECT * FROM user where userId=?";
  //public static final String SOFT_DELETE_USER = "UPDATE user SET isArchived=false WHERE email LIKE ?";
  public static final String UPDATE_USER = "UPDATE user set name=?,status=?,role=?,updatedOn=?," +
      "isVerified=?" +
      " WHERE id=?";
  public static final String GET_USER = "SELECT * from user where id = ?";
  public static final String GET_USER_COUNT = "SELECT count(*) from user";
  public static final String GET_USER_COUNT_BY_SEARCH = "SELECT count(*) from user where email " +
      "like ?";
  public static final String GET_ACTIVE_USER_COUNT = "SELECT count(*) from user where status like 'ACTIVE' AND email not in (select email from user_course_ref where courseId=?)";
  public static final String GET_ACTIVE_USER_COUNT_BY_SEARCH = "SELECT count(*) from user where " +
      "status like 'ACTIVE' AND email like ? and email not in (select email from user_course_ref " +
      "where courseId=?)";
  public static final String GET_USERS = "select * from user order by updatedOn desc limit ?,?";
  public static final String GET_USERS_BY_SEARCH = "select * from user where email like ? " +
      "order" +
      " by " +
      "updatedOn desc " +
      "limit ?,?";
  public static final String GET_ACTIVE_USERS = "select * from user where status like 'ACTIVE' AND email NOT IN (select email from user_course_ref where courseId=?) order by updatedOn desc limit ?,?";
  public static final String GET_ACTIVE_USERS_BY_SEARCH = "select * from user where status like " +
      "'ACTIVE' AND email NOT IN (select email from user_course_ref where courseId=?) and email " +
      "LIKE ?" +
      " order by updatedOn desc limit ?,?";
  public static final String GET_USER_ID = "select id from user where email =?";

  public static final String SET_USER_IMAGE = "UPDATE user SET userImage=?, name=? WHERE email=?";
  public static final String FETCH_ALL_EMAIL = "SELECT email FROM user WHERE subscription=true";
  public static final String DELETE_USER = "DELETE FROM user WHERE email=?";// used when transaction fails
  public static final String SELECT_BY_EMAIL = "SELECT * FROM user WHERE email=?";
  public static final String INSERT_USER = "INSERT INTO user (name,email,isVerified,role,isArchived,status,updatedOn,subscription,userImage) VALUES(?,?,?,?,?,?,?,?,?)";
  public static final String UPDATE_USER_EMAIL_STATUS = "UPDATE user SET isVerified=?,updatedOn=? WHERE id=? ";

  // verification token
  public static final String INSERT_USER_TOKEN = "INSERT INTO verification_token (userEmail,token,activatedOn,isArchived) VALUES(?,?,?,?)";
  public static final String UPDATE_USER_TOKEN = "UPDATE verification_token SET isArchived=? WHERE (id=? AND token=?)";
  public static final String DELETE_TOKEN = "DELETE FROM verification_token WHERE userEmail=?";// used when
  public static final String GET_VERIFICATION_ID = "select id from verification_token where userEmail=?";                                                  // transaction
  // fails

  //session
  public static final String CREATE_SESSION = "INSERT INTO session_token(email,userToken,createdOn) VALUES(?,?,?)";
  public static final String FETCH_SESSION = "SELECT * FROM session_token WHERE email=?";
  public static final String DELETE_SESSION = "DELETE FROM session_token WHERE email=?";

  // courses
  public static final String FETCH_ALL_COURSES = "SELECT c.*,u.userImage FROM course as c,user as u WHERE c.isArchived IS FALSE AND u.email=c.instructorEmail order by c.updatedOn desc limit ?,?";
  public static final String FETCH_ALL_PUBLISHED_COURSES = "SELECT c.*,u.userImage FROM course as c,user as u WHERE c.isArchived IS FALSE AND u.email=c.instructorEmail and c.courseStatus ='published' order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_PUBLISHED_COURSES_BY_SEARCH = "SELECT c.*,u.userImage FROM" +
      " course as c,user as u WHERE c.isArchived IS FALSE AND c.courseName like ? and u.email=c" +
      ".instructorEmail and c.courseStatus ='published' order by updatedOn desc limit ?,?";
  public static final String FETCH_COURSE = "SELECT c.*,u.userImage FROM course as c, user as u WHERE c.courseId=? and c.instructorEmail=u.email";
  public static final String INSERT_COURSE = "INSERT INTO course (courseName," +
      "assignmentStatus,courseDescriptionValue,courseTypeId,createdBy,createdOn,updatedBy," +
      "updatedOn,durationValue,isArchived,courseStatus,instructorEmail,startDate,endDate) VALUES" +
      "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  public static final String UPDATE_COURSE = "UPDATE course SET courseDescriptionValue=?,courseTypeId=?," +
      "createdBy=?,createdOn=?,updatedBy=?,updatedOn=?,durationValue=?,instructorEmail=?," +
      "courseName = ?,courseStatus=?,startDate=?, endDate=? WHERE courseId=?";

  public static final String PUBLISH_COURSE = "UPDATE course SET courseStatus='published' WHERE courseId=?";
  public static final String DELETE_ASSIGNMENT = "UPDATE assignment SET isArchived=false WHERE courseId=?";
  public static final String HARD_DELETE_ASSESSMENT_POLICY = "delete from assessment_policy where" +
      " course_id=?";
  public static final String HARD_DELETE_COURSE ="delete from course where courseId=?";
  public static final String DELETE_COURSE = "UPDATE course SET isArchived=TRUE,updatedOn=? " +
      "WHERE courseId=?";
  public static final String FETCH_ALL_AUTHORED_COURSES = "SELECT * FROM course WHERE instructorEmail=? order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_AUTHORED_COURSES_BY_SEARCH = "SELECT * FROM course WHERE " +
      "instructorEmail=? and courseName like ? order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_COURSE_PUBLISH_REQUEST = "SELECT * FROM course WHERE " +
      "courseStatus='approval_pending'";
  public static final String UPDATE_COURSE_PUBLISH_REQUEST = "UPDATE course SET courseStatus=?, " +
      "updatedOn = ? " +
      "WHERE courseId=?";
  //FETCH_ALL_AUTHORED_COURSES_WITHOUT_PAGINATION
  public static final String FETCH_ALL_AUTHORED_COURSES_WITHOUT_PAGINATION = "SELECT * FROM course WHERE instructorEmail=? order by updatedOn desc";
  public static final String FETCH_ALL_AUTHORED_COURSES_INITIAL_WITH_PAGINATION = "SELECT * FROM course " +
      "WHERE instructorEmail=? AND courseStatus = (SELECT courseStatus from course where " +
      "instructorEmail=? group by courseStatus having count(*)>0 limit 1) order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_AUTHORED_COURSES_INITIAL_FOR_ALL_USER_WITH_PAGINATION =
      "SELECT * FROM" +
      " course " +
      "WHERE courseStatus = (SELECT courseStatus from course group by courseStatus having count(*)>0 limit 1) order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_AUTHORED_COURSES_BY_STATUS_WITH_PAGINATION = "SELECT * FROM course " +
      "WHERE instructorEmail=? AND courseStatus = ? order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_AUTHORED_COURSES_BY_STATUS__FOR_ALL_USER_WITH_PAGINATION =
      "SELECT * FROM course " +
      "WHERE courseStatus = ? order by updatedOn desc limit ?,?";
  public static final String FETCH_ALL_USER_COUNT_FOR_COURSES = "SELECT count(*),userCourseStatus" +
      " FROM user_course_ref WHERE courseId =? group by userCourseStatus";
  public static final String FETCH_ALL_COURSE_COUNT_FOR_USER = "SELECT count(*),userCourseStatus" +
      " FROM user_course_ref WHERE email =? group by userCourseStatus";
  public static final String FETCH_ALL_AUTHORED_COURSE_COUNT_FOR_USER = "SELECT count(*)," +
      "courseStatus FROM course WHERE instructorEmail =? group by courseStatus";
  public static final String FETCH_ALL_AUTHORED_COURSE_COUNT_FOR_ALL_USER = "SELECT count(*)," +
      "courseStatus FROM course group by courseStatus";
  public static final String FETCH_ALL_COURSES_WITHOUT_PAGINATION = "SELECT * FROM course order by updatedOn desc";
  public static final String PUBLISH_ASSIGNMENT = "UPDATE course SET assignmentStatus=? WHERE courseId=?";

  public static final String TOTAL_ENROLLED_FOR_COURSE = "SELECT COUNT(*) FROM user_course_ref WHERE courseId=? AND status='APPROVED'";
  public static final String GET_MAX_COURSE_ID_FOR_AUTHOR = "select courseId from course where " +
      "courseName=? " +
      "and instructorEmail=?";

  public static final String GET_COUNT_COURSES = "SELECT count(*) from course WHERE courseStatus='published'";
  public static final String GET_COUNT_COURSES_BY_SEARCH = "SELECT count(*) from course WHERE " +
      "courseName like ? and " +
      "courseStatus='published'";
  public static final String GET_COUNT_REGISTERED_COURSES = "SELECT count(*) from user_course_ref WHERE email = ? AND status LIKE 'APPROVED'";
  public static final String GET_COUNT_REGISTERED_COURSES_BY_SEARCH = "SELECT count(*) from " +
      "user_course_ref WHERE courseName like ? and email = ? AND status LIKE 'APPROVED'";
  public static final String GET_AUTHORED_COUNT_COURSES = "SELECT count(*) from course WHERE instructorEmail=?";
  public static final String GET_AUTHORED_COUNT_COURSES_BY_SEARCH = "SELECT count(*) from course " +
      "WHERE instructorEmail=? and courseName like ?";
  public static final String GET_REQUESTED_COURSE_COUNT = "SELECT COUNT(*) FROM user_course_ref WHERE status like 'PENDING'";
  public static final String GET_REQUESTED_COURSE_COUNT_BY_SEARCH = "SELECT COUNT(*) FROM " +
      "user_course_ref WHERE courseName like ? and status like 'PENDING'";

  //assessment policy
  public static final String GET_ASSESSMENT_POLICY = "SELECT * FROM assessment_policy WHERE " +
      "course_id IN (:ids)";
  public static final String INSERT_ASSESSMENT_POLICY = "INSERT INTO assessment_policy " +
      "(`course_id`,`description`,`start_time`,`end_time`,`remainder`,`duration`,`attempts`," +
      "`is_full_screen_mode`,`updated_on`,`is_randomize_questions`,`show_answers`,`view_reports`," +
      "`toggles`" +
      ")" +
      " VALUES(?,?,?,?," +
      "?,?,?,?,?,?,?,?,?)";
  public static final String UPDATE_ASSESSMENT_POLICY = "UPDATE assessment_policy SET " +
      "`assignment_id` = ?,`description` = ?,`start_time` = ?,`end_time` = ?,`remainder` = ?," +
      "`duration` = ?,`attempts` = ?,`is_full_screen_mode` = ?,`updated_on` = ?, " +
      "`is_randomize_questions`=?,`show_answers`=?, `view_reports`=?,`toggles`=? WHERE `id` = ?;";
  public static final String UPDATE_ASSESSMENT_POLICY_ASSIGNMENT_ID = "UPDATE assessment_policy " +
      "SET `assignment_id` = (SELECT assignmentId FROM assignmentfile WHERE courseId = ? limit 1)" +
      "  WHERE `course_id` = ?";


}
