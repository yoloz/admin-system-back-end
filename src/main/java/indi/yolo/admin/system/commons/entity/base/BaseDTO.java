package indi.yolo.admin.system.commons.entity.base;

import lombok.Data;

/**
 * @author yoloz
 */
@Data
public class BaseDTO {

    private Integer pageNumber; // 页数
    private Integer pageSize; // 每页行数
    private Long totalRow; // 数据总量,第二页及之后没必要再查询数据总量
}
