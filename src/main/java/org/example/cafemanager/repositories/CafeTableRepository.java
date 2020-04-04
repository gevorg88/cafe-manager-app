package org.example.cafemanager.repositories;

import org.example.cafemanager.domain.CafeTable;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.table.OnlyTableProps;
import org.example.cafemanager.dto.table.SimpleTableProps;
import org.example.cafemanager.dto.table.TableWithOpenOrdersCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Repository
public interface CafeTableRepository extends CrudRepository<CafeTable, Long> {

    Collection<SimpleTableProps> findAllBy();

    OnlyTableProps findOneByName(@NotNull String name);

    CafeTable findCafeTableById(@NotNull Long id);

    CafeTable findCafeTableByNameAndIdIsNot(@NotNull String name, @NotNull Long id);

    Collection<OnlyTableProps> getAllByUserIdIs(@NotNull Long id);

    @Query(value = "select t.id as id, t.name as name, count(o.id) as orderCount from CafeTable t "
            + "left join Order o on o.cafeTable.id = t.id and o.status = :status "
            + "where t.user.id = :userId GROUP BY t.id")
    Collection<TableWithOpenOrdersCount> userTablesWithOrdersForStatus(
            @NotNull @Param("userId") Long userId,
            @NotNull @Param("status") Status status);

    CafeTable findCafeTableByIdAndUser(@NotNull Long tableId, @NotNull User user);
}
