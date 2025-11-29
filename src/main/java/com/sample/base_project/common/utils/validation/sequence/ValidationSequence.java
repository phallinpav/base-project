package com.sample.base_project.common.utils.validation.sequence;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({ ValidationSequence.FirstGroup.class,
        ValidationSequence.SecondGroup.class,
        ValidationSequence.ThirdGroup.class,
        ValidationSequence.FourthGroup.class,
        Default.class,
        ValidationSequence.LastGroup.class })
public interface ValidationSequence {
    interface FirstGroup {}
    interface SecondGroup {}
    interface ThirdGroup {}
    interface FourthGroup {}
    interface LastGroup {}
}
