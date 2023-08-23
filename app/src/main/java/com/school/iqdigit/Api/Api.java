package com.school.iqdigit.Api;

import com.school.iqdigit.Model.AchievementResponse;
import com.school.iqdigit.Model.ActivitiesPreviewResponse;
import com.school.iqdigit.Model.ActivitiesResponse;
import com.school.iqdigit.Model.AlbumnRespose;
import com.school.iqdigit.Model.AlertsPreviewResponse;
import com.school.iqdigit.Model.AlertsResponse;
import com.school.iqdigit.Model.AllTodayNoticeResponse;
import com.school.iqdigit.Model.AnsPreviewStaff;
import com.school.iqdigit.Model.AnsPreviewStaffResponse;
import com.school.iqdigit.Model.AnsPreviewStud;
import com.school.iqdigit.Model.AnsPreviewStudResponse;
import com.school.iqdigit.Model.AssSubResponse;
import com.school.iqdigit.Model.AssessmentResponse;
import com.school.iqdigit.Model.AssessmentStaffListResponse;
import com.school.iqdigit.Model.AssessmentStaffResponse;
import com.school.iqdigit.Model.AssignmentPreviewResponse;
import com.school.iqdigit.Model.AttendResponse;
import com.school.iqdigit.Model.BannerResponse;
import com.school.iqdigit.Model.BirthdaysResponse;
import com.school.iqdigit.Model.BookletResponse;
import com.school.iqdigit.Model.CalenderResponse;
import com.school.iqdigit.Model.ChaptersResponse;
import com.school.iqdigit.Model.CheckAnsStaffResponse;
import com.school.iqdigit.Model.CheckAnsStudResponse;
import com.school.iqdigit.Model.CircularsResponse;
import com.school.iqdigit.Model.CircularsStaff;
import com.school.iqdigit.Model.CircularsStaffResponse;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.ErrorResponse;
import com.school.iqdigit.Model.ExamStaffResponse;
import com.school.iqdigit.Model.ExamsResponse;
import com.school.iqdigit.Model.FeeReceiptistResponse;
import com.school.iqdigit.Model.FeeStatusResponse;
import com.school.iqdigit.Model.FeelistdataResponse;
import com.school.iqdigit.Model.GatewaydataResponse;
import com.school.iqdigit.Model.GetCurrentTime;
import com.school.iqdigit.Model.GetFeeOrderIdResponse;
import com.school.iqdigit.Model.GroupsResponse;
import com.school.iqdigit.Model.IcardCheckResponse;
import com.school.iqdigit.Model.ImageRespose;
import com.school.iqdigit.Model.LiveAttendanceResponse;
import com.school.iqdigit.Model.LiveClass1Response;
import com.school.iqdigit.Model.LiveStaffAttenResponse;
import com.school.iqdigit.Model.LiveStaffTimeResponse;
import com.school.iqdigit.Model.LiveTimetableResponse;
import com.school.iqdigit.Model.LoginResponse;
import com.school.iqdigit.Model.MaintenanceResponse;
import com.school.iqdigit.Model.MarksResponse;
import com.school.iqdigit.Model.MarksStaffResponse;
import com.school.iqdigit.Model.McqListResponse;
import com.school.iqdigit.Model.McqResultResponse;
import com.school.iqdigit.Model.MediaRespose;
import com.school.iqdigit.Model.NoticeStaffResponse;
import com.school.iqdigit.Model.OtpResponse;
import com.school.iqdigit.Model.PendingFeeResponse;
import com.school.iqdigit.Model.PhotosRespose;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.SchoolUrlResponse;
import com.school.iqdigit.Model.SchooldataResponse;
import com.school.iqdigit.Model.StaffProfileResponse;
import com.school.iqdigit.Model.StudentsResponse;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Model.SubmitpaymentResponse;
import com.school.iqdigit.Model.Timetableresponse;
import com.school.iqdigit.Model.TimrtableStaffResponse;
import com.school.iqdigit.Model.TutorialResponse;
import com.school.iqdigit.Model.UnitsResponse;
import com.school.iqdigit.Model.UserResponse;
import com.school.iqdigit.Model.feeamountresponse;
import com.school.iqdigit.Model.feelistresponse;
import com.school.iqdigit.Model.noticeresponse;
import com.school.iqdigit.Model.staffloginresponse;
import com.school.iqdigit.Model.userloginresponse;
import com.school.iqdigit.Modeldata.StudentsIcardResponse;
import com.school.iqdigit.Modeldata.StudyAddResponse;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {
    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> userlogin(
            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("userloginbyid")
    Call<userloginresponse> userloginbyid(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("sendotp")
    Call<OtpResponse> sendotp(
            @Field("phonenum") String phonenum,
            @Field("otpdigit") String otpdigit
    );

    @FormUrlEncoded
    @POST("checkactiveuser")
    Call<ErrorResponse> checkactiveuser(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("checkactiveteacher")
    Call<ErrorResponse> checkactivestaff(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("addonlinepaymentorderid")
    Call<GetFeeOrderIdResponse> addonlinepaymentorderid(
            @Field("id") String id,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("addalerts")
    Call<ErrorResponse> addalerts(
            @Field("staffid") String staffid,
            @Field("studentid") String studentid,
            @Field("title") String title,
            @Field("desc") String desc,
            @Field("class_id") String class_id,
            @Field("isclasschecked") String isclasschecked,
            @Field("isstuchecked") String isstuchecked,
            @Field("studgroupid") String studgroupid
    );

    @FormUrlEncoded
    @POST("app_registration")
    Call<ErrorResponse> addregistration(
            @Field("student_name") String student_name,
            @Field("father_name") String father_name,
            @Field("mother_name") String mother_name,
            @Field("state") String state,
            @Field("city") String city,
            @Field("mobile") String mobile,
            @Field("date_of_birth") String date_of_birth,
            @Field("class_reg") String class_reg
    );

    @FormUrlEncoded
    @POST("app_enquiry")
    Call<ErrorResponse> addenquiry(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("cat") String cat,
            @Field("detail") String detail,
            @Field("class") String itemclass);

    @Multipart
    @POST("upload")
    Call<DefaultResponse> uploadImage(
            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
            @Part("pdf\"; filename=\"myfile.pdf\" ") RequestBody pdf,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("class") RequestBody classid,
            @Part("subject") RequestBody subject,
            @Part("ldate") RequestBody ldate,
            @Part("cdate") RequestBody cdate,
            @Part("staff_id") RequestBody staff_id,
            @Part("assessment") RequestBody assessment,
            @Part("start_datetime") RequestBody start_datetime,
            @Part("end_datetime") RequestBody end_datetime,
            @Part("time_bound") RequestBody time_bound,
            @Part("unit") RequestBody unit,
            @Part("max") RequestBody max,
            @Part("mcq_marks") RequestBody mcq_marks,
            @Part("mcq_count") RequestBody mcq_count,
            @Part("negative_marks_count") RequestBody negative_marks_count
    );

    @Multipart
    @POST("upload")
    Call<DefaultResponse> uploadImageAlert(
            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("class") RequestBody classid,
            @Part("subject") RequestBody subject,
            @Part("ldate") RequestBody ldate,
            @Part("cdate") RequestBody cdate,
            @Part("staff_id") RequestBody staff_id
    );

    @Multipart
    @POST("uploadactivity")
    Call<DefaultResponse> uploadactivity(
            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("class") RequestBody classid,
            @Part("staff_id") RequestBody staff_id,
            @Part("ischecked") RequestBody ischecked,
            @Part("studgroupid") RequestBody studgroupid
    );

    @Multipart
    @POST("app_job_apply")
    Call<DefaultResponse> uploadResume(
            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody image,
            @Part("name") RequestBody name,
            @Part("post") RequestBody post,
            @Part("mobile") RequestBody mobile,
            @Part("email") RequestBody email
    );

    @Multipart
    @POST("submit_assessment")
    Call<DefaultResponse> uploadPdf(
            @Part("image\"; filename=\"myfile.pdf\" ") RequestBody image,
            @Part("id") RequestBody id,
            @Part("remarks") RequestBody remarks
    );

    @FormUrlEncoded
    @POST("staffloginbyphone")
    Call<staffloginresponse> staffloginbyphone(
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("addtokan")
    Call<ErrorResponse> addtokan(
            @Field("tokan") String tokan,
            @Field("id") String id,
            @Field("by") String by
    );

    @FormUrlEncoded
    @POST("submitpayment")
    Call<SubmitpaymentResponse> submitpayment(
            @Field("student_id") String student_id,
            @Field("f_ids") String f_ids,
            @Field("total_amount") String total_amount,
            @Field("orderid") String orderid
    );

    @FormUrlEncoded
    @POST("app_applyleave")
    Call<ErrorResponse> applyleave(
            @Field("staffid") String staffid,
            @Field("studentclass") String studentclass,
            @Field("studentid") String studentid,
            @Field("studentname") String studentname,
            @Field("leave_from") String leave_from,
            @Field("leave_to") String leave_to,
            @Field("reason") String reason,
            @Field("approval") Integer approval
    );

    @FormUrlEncoded
    @POST("attendance_taken")
    Call<ErrorResponse> submittattendance(@Field("at_std_class") String at_std_class,
                                          @Field("attendance_date") String attendance_date,
                                          @Field("at_std_subject") String at_std_subject,
                                          @Field("at_reg_no[]") ArrayList<String> at_reg_no,
                                          @Field("at_attendance[]") ArrayList<String> at_attendance,
                                          @Field("at_stud_name[]") ArrayList<String> at_stud_name,
                                          @Field("staff_id") String staff_id);

    @FormUrlEncoded
    @POST("check_assessment")
    Call<DefaultResponse> checkassessment(@Field("score") String score,
                                          @Field("check_remarks") String check_remarks,
                                          @Field("id") String id);

    @FormUrlEncoded
    @POST("reject_assessment")
    Call<DefaultResponse> rejectassessment(@Field("id") String id);

    @FormUrlEncoded
    @POST("mcq_ans_staff")
    Call<DefaultResponse> setStaffMcqAns(@Field("hw_id") String hw_id,
                                         @Field("mcq_sno") String mcq_sno,
                                         @Field("ans") String ans);

    @FormUrlEncoded
    @POST("mcq_ans_stud")
    Call<DefaultResponse> setStudentMcqAns(@Field("mcq_id") String mcq_id,
                                           @Field("studid") String studid,
                                           @Field("ansstud") String ansstud);

    @FormUrlEncoded
    @POST("anskey_done")
    Call<DefaultResponse> setans_done(@Field("hw_id") String hw_id);

    @FormUrlEncoded
    @POST("mcq_completed_stud")
    Call<DefaultResponse> setmcq_completed_stud(@Field("hw_id") String hw_id,
                                                @Field("studid") String studid);

    @FormUrlEncoded
    @POST("publish_result_mcq")
    Call<DefaultResponse> setpublish_result_mcq(@Field("hw_id") String hw_id);

    @FormUrlEncoded
    @POST("add_unit")
    Call<DefaultResponse> setadd_unit(@Field("es_classesid") String es_classesid,
                                      @Field("es_subjectid") String es_subjectid,
                                      @Field("unit_name") String unit_name);

    @FormUrlEncoded
    @POST("edit_unit")
    Call<DefaultResponse> edit_unit(@Field("unit_id") String unit_id,
                                    @Field("unit_name") String unit_name);

    @FormUrlEncoded
    @POST("add_chapter")
    Call<DefaultResponse> add_chapter(@Field("unit_id") String unit_id,
                                      @Field("chapter_name") String chapter_name);

    @FormUrlEncoded
    @POST("edit_chapter")
    Call<DefaultResponse> edit_chapter(@Field("chapter_id") String chapter_id,
                                       @Field("chapter_name") String chapter_name);

    @Multipart
    @POST("add_typed")
    Call<StudyAddResponse> edit_typed(@Part("chapter_id") RequestBody chapter_id,
                                      @Part("title") RequestBody title,
                                      @Part("lesson") RequestBody lesson,
                                      @Part("status") RequestBody status,
                                      @Part("user_type") RequestBody user_type,
                                      @Part("user_id") RequestBody user_id,
                                      @Part("running_id") RequestBody running_id);

    @Multipart
    @POST("add_lms_image")
    Call<StudyAddResponse> edit_lms_image(@Part("subject_id") RequestBody subject_id,
                                          @Part("book_name") RequestBody book_name,
                                          @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
                                          @Part("chapter_id") RequestBody chapter_id,
                                          @Part("book_desc") RequestBody book_desc,
                                          @Part("status") RequestBody status,
                                          @Part("user_type") RequestBody user_type,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("running_id") RequestBody running_id);

    @Multipart
    @POST("add_lms_pdf")
    Call<StudyAddResponse> edit_lms_pdf(@Part("subject_id") RequestBody subject_id,
                                        @Part("book_name") RequestBody book_name,
                                        @Part("image\"; filename=\"myfile.pdf\" ") RequestBody pdf,
                                        @Part("chapter_id") RequestBody chapter_id,
                                        @Part("book_desc") RequestBody book_desc,
                                        @Part("status") RequestBody status,
                                        @Part("user_type") RequestBody user_type,
                                        @Part("user_id") RequestBody user_id,
                                        @Part("running_id") RequestBody running_id);

    @Multipart
    @POST("add_lms_video")
    Call<StudyAddResponse> edit_lms_video(@Part("subject_id") RequestBody subject_id,
                                          @Part("book_name") RequestBody book_name,
                                          @Part("image") RequestBody media_url,
                                          @Part("chapter_id") RequestBody chapter_id,
                                          @Part("book_desc") RequestBody book_desc,
                                          @Part("status") RequestBody status,
                                          @Part("user_type") RequestBody user_type,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("running_id") RequestBody running_id);

    @Multipart
    @POST("add_typed")
    Call<StudyAddResponse> add_typed(@Part("chapter_id") RequestBody chapter_id,
                                     @Part("title") RequestBody title,
                                     @Part("lesson") RequestBody lesson,
                                     @Part("status") RequestBody status,
                                     @Part("user_type") RequestBody user_type,
                                     @Part("user_id") RequestBody user_id);

    @Multipart
    @POST("add_lms_image")
    Call<StudyAddResponse> add_lms_image(@Part("subject_id") RequestBody subject_id,
                                         @Part("book_name") RequestBody book_name,
                                         @Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
                                         @Part("chapter_id") RequestBody chapter_id,
                                         @Part("book_desc") RequestBody book_desc,
                                         @Part("status") RequestBody status,
                                         @Part("user_type") RequestBody user_type,
                                         @Part("user_id") RequestBody user_id);

    @Multipart
    @POST("add_lms_pdf")
    Call<StudyAddResponse> add_lms_pdf(@Part("subject_id") RequestBody subject_id,
                                       @Part("book_name") RequestBody book_name,
                                       @Part("image\"; filename=\"myfile.pdf\" ") RequestBody pdf,
                                       @Part("chapter_id") RequestBody chapter_id,
                                       @Part("book_desc") RequestBody book_desc,
                                       @Part("status") RequestBody status,
                                       @Part("user_type") RequestBody user_type,
                                       @Part("user_id") RequestBody user_id);

    @Multipart
    @POST("add_lms_video")
    Call<StudyAddResponse> add_lms_video(@Part("subject_id") RequestBody subject_id,
                                         @Part("book_name") RequestBody book_name,
                                         @Part("image") RequestBody media_url,
                                         @Part("chapter_id") RequestBody chapter_id,
                                         @Part("book_desc") RequestBody book_desc,
                                         @Part("status") RequestBody status,
                                         @Part("user_type") RequestBody user_type,
                                         @Part("user_id") RequestBody user_id);

    @FormUrlEncoded
    @POST("marks_entered")
    Call<DefaultResponse> marks_entered(@Field("exam_detailsid") String exam_detailsid,
                                        @Field("es_marksstudentid[]") ArrayList<String> es_marksstudentid,
                                        @Field("es_marksobtined[]") ArrayList<String> es_marksobtined);

    @FormUrlEncoded
    @POST("lock_marks")
    Call<DefaultResponse> lock_marks(@Field("exam_detailsid") String exam_detailsid);

    @FormUrlEncoded
    @POST("publish_result_mcq_single")
    Call<DefaultResponse> publish_result_mcq_single(@Field("hw_id") String hw_id,
                                                    @Field("stud_id") String stud_id);

    @FormUrlEncoded
    @POST("delete_assessment")
    Call<DefaultResponse> delete_assessment(@Field("hw_id") String hw_id);

    @Multipart
    @POST("stud_photo_update")
    Call<DefaultResponse> stud_photo_update(@Part("stud_id") RequestBody stud_id,
                                            @Part("stud_img\"; filename=\"myfile.jpg\" ") RequestBody file);

    @Multipart
    @POST("staff_photo_update")
    Call<DefaultResponse> staff_photo_update(@Part("staff_id") RequestBody staff_id,
                                            @Part("staff_img\"; filename=\"myfile.jpg\" ") RequestBody file);

    @FormUrlEncoded
    @POST("photo_status_update")
    Call<DefaultResponse> photo_status_update(@Field("stud_id") String stud_id ,@Field("photo_status") String photo_status);

    @FormUrlEncoded
    @POST("photo_status_update_class")
    Call<DefaultResponse> photo_status_update_class(@Field("photo_status") String photo_status ,@Field("class_id") String class_id);

    //reject_assessment
    @GET("allusers/{phone}")
    Call<UserResponse> getUsers(
            @Path("phone") String phone
    );

    @GET("getschoolDetails")
    Call<SchooldataResponse> getSchoolDetails();

    @GET("receipt/{stud_id}")
    Call<FeeReceiptistResponse> getAllreceipt(
            @Path("stud_id") String stuid
    );

    @GET("getreceipt/{fid}")
    Call<FeelistdataResponse> getAReceipt(
            @Path("fid") String fid
    );

    @GET("getexamlist/{stu_id}")
    Call<ExamsResponse> getExamList(
            @Path("stu_id") String stu_id
    );

    @GET("liveclassurl")
    Call<LiveClass1Response> getliveclassurl();

    @GET("getactivities_new/{studid}")
    Call<ActivitiesResponse> getActivities(
            @Path("studid") String studid
    );

    @GET("getcalender/{sdate}/{edate}")
    Call<CalenderResponse> getcalender(
            @Path("sdate") String sdate,
            @Path("edate") String edate
    );

    @GET("getschoollurl")
    Call<SchoolUrlResponse> getschoollurl();

    @GET("getalerts/{id}")
    Call<AlertsResponse> getAlerts(
            @Path("id") String id
    );

    @GET("getexammarks/{stu_id}/{exam_id}/{class_id}")
    Call<MarksResponse> getExamMarks(
            @Path("stu_id") String stu_id,
            @Path("exam_id") String exam_id,
            @Path("class_id") String class_id);


    @GET("getclasses/{id}")
    Call<ClassResponse> getClasses(
            @Path("id") String sid
    );

    @GET("getstudgroups")
    Call<GroupsResponse> getGroup(
    );

    @GET("getclasses_lms/{id}")
    Call<ClassResponse> getClasses_lms(
            @Path("id") String sid
    );

    @GET("getsubjects/{classid}")
    Call<SubjectResponse> getSubjects(
            @Path("classid") String classid
    );

    @GET("getsubjects_lms/{classid}")
    Call<SubjectResponse> getSubjects_lms(
            @Path("classid") String classid
    );

    @GET("getStudentsbyclass/{classid}")
    Call<StudentsResponse> getStudentsbyclass(
            @Path("classid") String classid
    );

    @GET("getStudentsforattendance/{classid}/{attdate}")
    Call<StudentsResponse> getStudentsforattendance(
            @Path("classid") String classid,
            @Path("attdate") String attdate
    );

    @GET("alltodaynotice/{date}")
    Call<AllTodayNoticeResponse> getAlltodayNotice(
            @Path("date") String date
    );

    @GET("alltodaynotice_staff/{date}")
    Call<NoticeStaffResponse> getalltodaynotice_staff(
            @Path("date") String date
    );

    @GET("gettodaynotice/{date}")
    Call<noticeresponse> getTodayNotice(
            @Path("date") String date
    );

    @GET("getuserprofile/{id}")
    Call<ProfileResponse> getUserProfile(
            @Path("id") String id
    );

    @GET("getattendence/{id}/{sdate}/{edate}")
    Call<AttendResponse> getAttendance(
            @Path("id") String id,
            @Path("sdate") String sdate,
            @Path("edate") String edate

    );

    @GET("getassignment/{classid}/{date}")
    Call<AssSubResponse> getAssSubject(
            @Path("classid") String classid,
            @Path("date") String date

    );

    @GET("getfeedetails/{stu_id}/{uptodate}")
    Call<feeamountresponse> getFeeamountDeatils(
            @Path("stu_id") String stu_id,
            @Path("uptodate") String uptodate

    );

    @GET("getbanner")
    Call<BannerResponse> getAllBanner();

    @GET("getfeestruc_new/{stu_id}")
    Call<feelistresponse> getFeeMonth(
            @Path("stu_id") String stu_id);

    @GET("gatewaydetails_multibank/{studid}")
    Call<GatewaydataResponse> getGatewaydata(
            @Path("studid") String studid);

    @GET("getTimeTable/{classid}/{day}")
    Call<Timetableresponse> getTimeTable(
            @Path("classid") String classid,
            @Path("day") String day);

    @GET("getachievements")
    Call<AchievementResponse> getachievements();

    @GET("getfee")
    Call<AchievementResponse> getfeestructure();

    @GET("getunits/{es_classesid}/{es_subjectid}")
    Call<UnitsResponse> getunits(
            @Path("es_classesid") String es_classesid,
            @Path("es_subjectid") String es_subjectid
    );

    @GET("getchapters/{es_classesid}/{es_subjectid}")
    Call<ChaptersResponse> getchapters(
            @Path("es_classesid") String es_classesid,
            @Path("es_subjectid") String es_subjectid
    );

    @GET("getbooklets/{chapterid}")
    Call<BookletResponse> getbooklets(
            @Path("chapterid") String chapterid);

    @GET("gettutorials/{chapterid}")
    Call<TutorialResponse> gettutorials(
            @Path("chapterid") String chapterid
    );

    @GET("getmedia/{chapterid}")
    Call<MediaRespose> getmedia(
            @Path("chapterid") String chapterid
    );

    @GET("getimage/{chapterid}")
    Call<ImageRespose> getimage(
            @Path("chapterid") String chapterid
    );

    @GET("app_check")
    Call<MaintenanceResponse> getappcheck();

    @GET("getassessment_list/{stud_id}/{class_id}/{stage}")
    Call<AssessmentResponse> getAssessmentList(
            @Path("stud_id") String stud_id,
            @Path("class_id") String class_id,
            @Path("stage") String stage
    );

    @GET("getassessment_list_staff/{staff_id}")
    Call<AssessmentStaffResponse> getstaffAssessmentList(
            @Path("staff_id") String staff_id);

    //get_assessment_list_root

    @GET("get_assessment_list_root/{staff_id}/{hw_id}/{stage}")
    Call<AssessmentStaffListResponse> getAssessmentstaffList(
            @Path("staff_id") String staff_id,
            @Path("hw_id") String hw_id,
            @Path("stage") String stage);

    @GET("getTimeTable_staff/{staffid}/{day}")
    Call<TimrtableStaffResponse> getTimeTableStaff(
            @Path("staffid") String staffid,
            @Path("day") String day);

    @GET("getcirculars")
    Call<CircularsResponse> getCircular();


    @GET("getcirculars_staff")
    Call<CircularsStaffResponse> getcirculars_staff();

    @GET("getbirthdays")
    Call<BirthdaysResponse> getBirthdays();

    @GET("getdatetime")
    Call<GetCurrentTime> getCurrentDateTime();

    @GET("mcq_layout/{hw_id}")
    Call<McqListResponse> getMcqListResponse(
            @Path("hw_id") String hw_id);

    @GET("check_if_answered_staff/{hw_id}")
    Call<CheckAnsStaffResponse> getCheckAnsStaffResponse(
            @Path("hw_id") String hw_id);

    @GET("check_if_answered_stud/{hw_id}/{studid}")
    Call<CheckAnsStudResponse> getCheckAnsStudResponse(
            @Path("hw_id") String hw_id,
            @Path("studid") String studid);

    @GET("mcq_show_result_mcq/{hw_id}/{studid}")
    Call<McqResultResponse> getMcqResultResponse(
            @Path("hw_id") String hw_id,
            @Path("studid") String studid);

    @GET("getexamlist_marksentry/{es_classesid}/{es_subjectid}")
    Call<ExamStaffResponse> getexamlist_marksentry(
            @Path("es_classesid") String es_classesid,
            @Path("es_subjectid") String es_subjectid
    );

    @GET("getStudentsbyexamlist/{es_classesid}/{exam_detailsid}")
    Call<MarksStaffResponse> getStudentsbyexamlist(
            @Path("es_classesid") String es_classesid,
            @Path("exam_detailsid") String exam_detailsid
    );

    @GET("ans_preview_staff/{hw_id}")
    Call<AnsPreviewStaffResponse> getAnsPreviewStaffResponse(
            @Path("hw_id") String hw_id);

    @GET("ans_preview_stud/{hw_id}/{studid}")
    Call<AnsPreviewStudResponse> getAnsPreviewStudResponse(
            @Path("hw_id") String hw_id,
            @Path("studid") String studid);

    @GET("getassignment_staff/{staff_id}/{date}")
    Call<AssignmentPreviewResponse> getassignments_staffResponse(
            @Path("staff_id") String staff_id,
            @Path("date") String date);

    @GET("getalerts_staff/{staff_id}")
    Call<AlertsPreviewResponse> getalerts_staffResponse(
            @Path("staff_id") String staff_id);

    @GET("getactivities_staff/{staff_id}")
    Call<ActivitiesPreviewResponse> getactivities_staffResponse(
            @Path("staff_id") String staff_id);

    @GET("pendingfee_alert/{studentid}")
    Call<PendingFeeResponse> getpendingfeeResponse(
            @Path("studentid") String studentid
    );

    @GET("live_timetable/{classid}/{day}")
    Call<LiveTimetableResponse> getLiveTimetableResponse(
            @Path("classid") String classid,
            @Path("day") String day
    );

    @GET("live_timetable_staff/{staff_id}/{day}")
    Call<LiveStaffTimeResponse> getlive_timetable_staff(
            @Path("staff_id") String staff_id,
            @Path("day") String day
    );

    @GET("live_attendance_class/{classid}/{sdate}")
    Call<LiveStaffAttenResponse> getLiveStaffAttenResponse(
            @Path("classid") String classid,
            @Path("sdate") String sdate
    );

    @GET("live_attendance_stud/{studid}/{sdate}")
    Call<LiveAttendanceResponse> getLiveAttendanceResponse(
            @Path("studid") String studid,
            @Path("sdate") String sdate
    );

    @GET("get_photoalbum")
    Call<AlbumnRespose> getalbumns();

    @GET("get_photos/{al_id}")
    Call<PhotosRespose> get_photos(
            @Path("al_id") String al_id
    );

    @GET("photo_check/{stud_id}")
    Call<IcardCheckResponse> get_photo_check(
            @Path("stud_id") String stud_id
    );

    @GET("getStudentsbyclass_photo/{classid}")
    Call<StudentsIcardResponse> getStudentsbyclass_photo(
            @Path("classid") String classid
    );

    @GET("getstaffprofile/{id}")
    Call<StaffProfileResponse> getgetstaffprofile(
            @Path("id") String id
    );

    @GET("validate_payfee_attempt/{id}")
    Call<FeeStatusResponse> getvalidatePayfeeAttempt(
            @Path("id") String id
    );
}
