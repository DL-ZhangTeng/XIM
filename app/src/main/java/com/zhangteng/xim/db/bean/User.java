package com.zhangteng.xim.db.bean;

/**
 * Created by swing on 2018/5/25.
 */
public class User extends com.zhangteng.xim.bmob.entity.User {
    private Long id;

    public User() {
    }

    public User(Long id, String objectId, String mobilePhoneNumber, String email
            , Long sex, Long age, Long schoolId, Long roleId, Long gradeId, Long cityId
            , Long areaId, Long provinceId, Long classId, String realName) {
        super();
        this.id = id;
        this.setObjectId(objectId);
        this.setMobilePhoneNumber(mobilePhoneNumber);
        this.setEmail(email);
        if (sex != null) {
            this.setSex(sex.intValue());
        }
        if (age != null) {
            this.setAge(age.intValue());
        }
        if (schoolId != null) {
            this.setSchoolId(schoolId.intValue());
        }
        if (roleId != null) {
            this.setRoleId(roleId.intValue());
        }
        if (gradeId != null) {
            this.setGradeId(gradeId.intValue());
        }
        if (cityId != null) {
            this.setCityId(cityId.intValue());
        }
        if (areaId != null) {
            this.setAreaId(areaId.intValue());
        }
        if (provinceId != null) {
            this.setProvinceId(provinceId.intValue());
        }
        if (classId != null) {
            this.setClassId(classId.intValue());
        }
        this.setRealName(realName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
