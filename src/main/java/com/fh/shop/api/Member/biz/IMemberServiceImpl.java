package com.fh.shop.api.Member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.Area.mapper.AreaMapper;
import com.fh.shop.api.Member.mapper.MemberMapper;
import com.fh.shop.api.Member.po.Member;
import com.fh.shop.api.Member.vo.MemberVo;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.mq.MQSender;
import com.fh.shop.api.mq.MailMessage;
import com.fh.shop.api.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class IMemberServiceImpl implements IMemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private MQSender mqSender;

    @Override
    public ServerResponse register(Member member) {
        if (StringUtils.isBlank(member.getMemberName())){
            ServerResponse.error(ResponseEnum.MEMBERNAME_IS_NULL);
        }
        if (StringUtils.isBlank(member.getPassword())){
            ServerResponse.error(ResponseEnum.PASSWORD_IS_NULL);
        }
        if (StringUtils.isBlank(member.getRealName())){
            ServerResponse.error(ResponseEnum.REALNAME_IS_NULL);
        }
        if (StringUtils.isBlank(member.getBirthday())){
            ServerResponse.error(ResponseEnum.BIRTHDAY_IS_NULL);
        }
        if (StringUtils.isBlank(member.getMail())){
            ServerResponse.error(ResponseEnum.MAIL_IS_NULL);
        }
        if (StringUtils.isBlank(member.getPhone())){
            ServerResponse.error(ResponseEnum.PHONE_IS_NULL);
        }
        QueryWrapper<Member> memeberWrapper = new QueryWrapper<>();
        memeberWrapper.eq("memberName",member.getMemberName());
        Member memberName = memberMapper.selectOne(memeberWrapper);
        if (memberName!=null){
            return  ServerResponse.error(ResponseEnum.MEMBERNAME_IS_CUNZAI);
        }
        QueryWrapper<Member> phoneWrapper = new QueryWrapper<>();
        phoneWrapper.eq("phone",member.getPhone());
        Member phone = memberMapper.selectOne(phoneWrapper);
        if (phone!=null){
            return  ServerResponse.error(ResponseEnum.PHONE_IS_CUNZAI);
        }
        //判断邮箱是否唯一
        QueryWrapper<Member> mailWrapper = new QueryWrapper<>();
        mailWrapper.eq("mail",member.getMail());
        Member mail = memberMapper.selectOne(mailWrapper);
        if (mail!=null){
            return  ServerResponse.error(ResponseEnum.MAIL_IS_CUNZAI);
        }
        //注册会员
        memberMapper.addMember(member);
        //给注册的会员发邮件
        emailHelper.sendEmail(member.getMail(),"注册成功","恭喜你注册成功！");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse query(Member member) {
        if (StringUtils.isNotBlank(member.getMemberName())){
            QueryWrapper<Member> memeberWrapper = new QueryWrapper<>();
            memeberWrapper.eq("memberName",member.getMemberName());
            Member memberName = memberMapper.selectOne(memeberWrapper);
            if (memberName!=null){
                return ServerResponse.success(memberName);
            }
            return ServerResponse.success();
        }
        if (StringUtils.isNotBlank(member.getPhone())){
            QueryWrapper<Member> phoneWrapper = new QueryWrapper<>();
            phoneWrapper.eq("phone",member.getPhone());
            Member phone = memberMapper.selectOne(phoneWrapper);
            if(phone!=null){
                return ServerResponse.success(phone);
            }
            return ServerResponse.success();
        }
        if (StringUtils.isNotBlank(member.getMail())){
            QueryWrapper<Member> mailWrapper = new QueryWrapper<>();
            mailWrapper.eq("mail",member.getMail());
            Member mail = memberMapper.selectOne(mailWrapper);
            if (mail!=null){
                return ServerResponse.success(mail);
            }
            return ServerResponse.success();
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(Member member) {
        String memberName = member.getMemberName();
        String password = member.getPassword();
        //验证用户名密码是否为空
        if (StringUtils.isEmpty(memberName)){
            return ServerResponse.error(ResponseEnum.MEMBERNAME_IS_NULL);
        }
        if (StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_NULL);
        }
        //验证用户是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName",memberName);
        Member selectOne = memberMapper.selectOne(queryWrapper);
        if (selectOne==null){
            return ServerResponse.error(ResponseEnum.MEMBER_IS_ERROR);
        }
        //验证密码是否相等
        if (!selectOne.getPassword().equals(password)){
            return ServerResponse.error(ResponseEnum.PASSWORD_IS_ERROR);
        }
        //准备组装的数据
        MemberVo memberVo = new MemberVo();
        memberVo.setId(selectOne.getId());
        memberVo.setMemberName(selectOne.getMemberName());
        memberVo.setRealName(selectOne.getRealName());
        String uuid = UUID.randomUUID().toString();
        memberVo.setUuid(uuid);
        String jsonString = JSONObject.toJSONString(memberVo);
        String memberJson = null;
        try {
            memberJson = Base64.getEncoder().encodeToString(jsonString.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //使用秘钥和准备好的数据生成签名信息
        //生成用户信息所对应的签名
        String secret = Md5Util.secret(memberJson, SystemConst.SECRET);
        String encode=null;
        try {
            //对签名也进行base64编码
             encode = Base64.getEncoder().encodeToString(secret.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //将明文+签名组合起来
        String result=memberJson+"."+encode;
        //处理超时
        RedisUtil.setEx(KeyUtils.memberKey(memberVo.getId(),memberVo.getUuid()),KeyUtils.MEMBER_EXPIRE,"csw");
        //发送邮件
//        emailHelper.sendEmail(selectOne.getMail(),"登录成功",selectOne.getRealName()+"在"+ DateTimeUtils.date2str(new Date(),DateTimeUtils.FULL_YEAR)+"登录了！");
        MailMessage mailMessage = new MailMessage();
        mailMessage.setMail(selectOne.getMail());
        mailMessage.setTitle("登录成功");
        mailMessage.setRealName(selectOne.getRealName());
        mailMessage.setContent(selectOne.getRealName()+"在"+ DateTimeUtils.date2str(new Date(),DateTimeUtils.FULL_YEAR)+"登录了！");
        mqSender.sendMailMessage(mailMessage);
        //响应数据给客户端
        return ServerResponse.success(result);
    }
}
