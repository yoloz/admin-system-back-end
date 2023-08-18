package indi.yolo.admin.system.commons.entity.base;

/**
 * @author yoloz
 */
public interface BaseMapStruct<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    D toVo(E entity);
}
