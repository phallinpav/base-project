package com.sample.base_project.common.base.view;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class GetBaseParam<ID> extends AuthParam implements Serializable {
    @NotNull
    protected ID uuid;
    protected boolean nullable;
    protected boolean mapRef;
    protected Set<String> mapKeyInclude;
    protected Set<String> mapKeyExclude;

    public static <ID> GetBaseParam<ID> of(ID uuid) {
        return of(uuid, false, false);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, IAuthParam authParam) {
        return of(uuid, false, false, authParam, null, null);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, IAuthParam authParam, boolean mapRef) {
        return of(uuid, mapRef, false, authParam, null, null);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, IAuthParam authParam, boolean mapRef, boolean nullable) {
        return of(uuid, mapRef, nullable, authParam, null, null);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, IAuthParam authParam, boolean mapRef, boolean nullable, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        return of(uuid, mapRef, nullable, authParam, mapKeyInclude, mapKeyExclude);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, boolean mapRef) {
        return of(uuid, mapRef, false, null, null, null);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, boolean mapRef, IAuthParam authParam) {
        return of(uuid, mapRef, false, authParam, null, null);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, boolean mapRef, boolean nullable) {
        return of (uuid, mapRef, nullable, null, null, null);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        return of (uuid, true, false, null, mapKeyInclude, mapKeyExclude);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, boolean mapRef, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        return of (uuid, mapRef, false, null, mapKeyInclude, mapKeyExclude);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, boolean mapRef, boolean nullable, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        return of (uuid, mapRef, nullable, null, mapKeyInclude, mapKeyExclude);
    }

    public static <ID> GetBaseParam<ID> of(ID uuid, boolean mapRef, boolean nullable, IAuthParam authParam, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        var param = GetBaseParam.<ID>builder()
                .uuid(uuid)
                .mapRef(mapRef)
                .mapKeyInclude(mapKeyInclude)
                .mapKeyExclude(mapKeyExclude)
                .nullable(nullable)
                .build();
        param.setAuthParam(authParam);
        return param;
    }
}
