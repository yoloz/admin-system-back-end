package indi.yolo.admin.system.commons.entity.base;

/**
 * @param <D> dto
 * @param <E> entity
 * @author yoloz
 */
public interface BaseMapStruct<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    D toVo(E entity);
}
