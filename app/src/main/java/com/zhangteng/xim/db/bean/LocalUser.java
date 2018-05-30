package com.zhangteng.xim.db.bean;

import com.zhangteng.swiperecyclerview.bean.GroupInfo;
import com.zhangteng.xim.bmob.entity.Friend;
import com.zhangteng.xim.bmob.entity.User;

/**
 * Created by swing on 2018/5/25.
 */
public class LocalUser extends User {
    private Long id;

    private GroupInfo groupInfo;

    public LocalUser() {
    }

    public LocalUser(Long id, String objectId, String mobilePhoneNumber, String email
            , Long sex, Long age, Long schoolId, Long roleId, Long gradeId, Long cityId
            , Long areaId, Long provinceId, Long classId, String realName, String username, String avatar
            , Long groupNum, String title, Long position, Long total) {
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
        this.setUsername(username);
        this.setIcoPath(avatar);

        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupNum(groupNum.intValue());
        groupInfo.setTitle(title);
        groupInfo.setPosition(position.intValue());
        groupInfo.setTotal(total.intValue());
        this.setGroupInfo(groupInfo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static LocalUser getLocalUser(Friend friend) {
        User user = friend.getFriendUser();
        LocalUser localUser = new LocalUser();
        localUser.setObjectId(user.getObjectId());
        localUser.setMobilePhoneNumber(user.getMobilePhoneNumber());
        localUser.setEmail(user.getEmail());
        localUser.setSex(user.getSex());
        localUser.setAge(user.getAge());
        localUser.setSchoolId(user.getSchoolId());
        localUser.setRoleId(user.getRoleId());
        localUser.setGradeId(user.getGradeId());
        localUser.setCityId(user.getCityId());
        localUser.setAreaId(user.getAreaId());
        localUser.setProvinceId(user.getProvinceId());
        localUser.setClassId(user.getClassId());
        localUser.setRealName(user.getRealName());
        localUser.setUsername(user.getUsername());
        localUser.setIcoPath(user.getIcoPath());
        localUser.setGroupInfo(friend.getGroupInfo());
        return localUser;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
