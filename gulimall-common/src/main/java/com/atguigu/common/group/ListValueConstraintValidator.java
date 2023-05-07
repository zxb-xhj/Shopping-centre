package com.atguigu.common.group;

import lombok.var;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
//com.atguigu.common.valid.ListValue.message=必须为指定的数据
//        ValidationMessages.properties
/**
 * @Author: xhj
 * @Date: 2023/03/14/16:07
 * @Description:
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    private Set<Integer> set = new HashSet<>();
    @Override
    public void initialize(ListValue listValue) {
        int[] vals = listValue.val();
        for (int val : vals) {
            set.add(val);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value);
    }
}
