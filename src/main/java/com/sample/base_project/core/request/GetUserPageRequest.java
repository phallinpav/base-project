package com.sample.base_project.core.request;

import com.sample.base_project.common.base.view.GetBasePageParam;
import com.sample.base_project.common.utils.common.StringUtils;
import com.sample.base_project.common.utils.filter.BaseSpecification;
import com.sample.base_project.common.utils.filter.SpecificationComposition;
import com.sample.base_project.common.utils.filter.SpecificationUtils;
import com.sample.base_project.core.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class GetUserPageRequest extends GetBasePageParam<User> {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneCode;
    private String phoneNum;

    @Override
    public SpecificationComposition<User> toSpecCom() {
        var specCom = super.toSpecCom();
        List<BaseSpecification<User>> list = new ArrayList<>();

        if (StringUtils.isNotBlank(firstName)) {
            list.add(SpecificationUtils.compareLike("firstName", firstName, true));
        }
        if (StringUtils.isNotBlank(lastName)) {
            list.add(SpecificationUtils.compareLike("lastName", lastName, true));
        }
        if (StringUtils.isNotBlank(email)) {
            list.add(SpecificationUtils.compareLike("email", email, true));
        }

        specCom.setOtherSpec(SpecificationComposition.with(list));
        return specCom;
    }
}
