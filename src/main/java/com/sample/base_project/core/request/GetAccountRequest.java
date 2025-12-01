package com.sample.base_project.core.request;

import com.sample.base_project.common.base.view.GetBasePageParam;
import com.sample.base_project.common.utils.common.ObjectUtils;
import com.sample.base_project.common.utils.filter.BaseSpecification;
import com.sample.base_project.common.utils.filter.SpecificationComposition;
import com.sample.base_project.common.utils.filter.SpecificationUtils;
import com.sample.base_project.common.utils.validation.common.FixedValue;
import com.sample.base_project.core.constants.CurrencyEnum;
import com.sample.base_project.core.model.Account;
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
public class GetAccountRequest extends GetBasePageParam<Account> {

    private List<String> userUuids;
    private List<@FixedValue(classes = CurrencyEnum.class) String> currencies;
    private List<String> statuses;

    @Override
    public SpecificationComposition<Account> toSpecCom() {
        var specCom = super.toSpecCom();
        List<BaseSpecification<Account>> list = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(userUuids)) {
            list.add(SpecificationUtils.compareIn("userUuid", userUuids));
        }
        if (ObjectUtils.isNotEmpty(currencies)) {
            list.add(SpecificationUtils.compareIn("currency", currencies));
        }
        if (ObjectUtils.isNotEmpty(statuses)) {
            list.add(SpecificationUtils.compareIn("status", statuses));
        }

        specCom.setOtherSpec(SpecificationComposition.with(list));
        return specCom;
    }
}
