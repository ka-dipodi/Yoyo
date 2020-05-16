package com.fgy.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 *获取所有的属性值为空属性的数组，避免对象赋值为null
 * 用于把 form 提交的表单中数据封装在 bean中；
 * @prame 操作对象
 * @return 属性名称的数组
 */
public class MyBeanUtils {
   public static String[] getNullPropertyNames(Object source){
       BeanWrapper beanWrapper = new BeanWrapperImpl(source);
       PropertyDescriptor[] pds=beanWrapper.getPropertyDescriptors();
       List<String> nullPropertyNamaes=new ArrayList<>();
       for(PropertyDescriptor pd : pds){
            String propertyName = pd.getName();
            if(beanWrapper.getPropertyValue(propertyName)==null){
                nullPropertyNamaes.add(propertyName);
            }
       }
       return nullPropertyNamaes.toArray(new String[nullPropertyNamaes.size()]);
   }
}
