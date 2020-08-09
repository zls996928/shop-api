package com.fh.shop.api.order.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.mapper.AddressMapper;
import com.fh.shop.api.order.po.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<OrderInfo> findList(Long memberId) {
        QueryWrapper<OrderInfo> recipientQueryWrapper = new QueryWrapper<>();
        recipientQueryWrapper.eq("memberId",memberId);
        List<OrderInfo> recipientList = addressMapper.selectList(recipientQueryWrapper);
        return recipientList;
    }

    @Override
    public ServerResponse addAddress(OrderInfo address) {
        addressMapper.insert(address);
        return ServerResponse.success();
    }





    @Override
    public ServerResponse addOrderData(Long memberId, OrderInfo orderInfo) {
        orderInfo.setId(memberId);
        if(orderInfo.getId()==null){
            addressMapper.insert(orderInfo);
        }else {
            addressMapper.updateById(orderInfo);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse getOrderById(Long memberId, OrderInfo orderInfo) {
        orderInfo.setId(memberId);
        OrderInfo order = addressMapper.selectOne(new QueryWrapper<OrderInfo>( ).eq("id", orderInfo.getId()));
        return ServerResponse.success(order);
    }
}
