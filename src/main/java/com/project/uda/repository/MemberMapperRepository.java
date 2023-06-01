package com.project.uda.repository;

import com.project.uda.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapperRepository {
    List<Member> getUserList(Map<String, Object> paramMap);
}
