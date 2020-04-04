package org.example.cafemanager;

import org.example.cafemanager.domain.*;
import org.example.cafemanager.domain.enums.Role;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.order.OrderDetails;
import org.example.cafemanager.dto.order.ProductInOrderReq;
import org.example.cafemanager.dto.order.UpdateProductInOrderDto;
import org.example.cafemanager.dto.product.CreateProductRequest;
import org.example.cafemanager.dto.product.SimpleProductProps;
import org.example.cafemanager.dto.table.OnlyTableProps;
import org.example.cafemanager.dto.table.SimpleTableProps;
import org.example.cafemanager.dto.table.TableCreateRequestBody;
import org.example.cafemanager.dto.table.TableWithOpenOrdersCount;
import org.example.cafemanager.dto.user.CreateUserRequest;
import org.example.cafemanager.dto.user.UpdateUserRequestBody;
import org.example.cafemanager.dto.user.UserPublicProfile;

import java.util.Arrays;
import java.util.List;

public class EntitiesBuilder {
    public final static String email = "test@test.test";

    public static User createUser() {
        User u = new User();
        u.setEmail(email);
        u.setUsername(Util.randomString(6));
        u.setPassword(Util.randomString(6));
        u.setFirstName(Util.randomString(6));
        u.setLastName(Util.randomString(6));
        u.setPassword(Util.randomString(6));
        u.setRole(Role.WAITER);
        return u;
    }

    public static User createUser(String username) {
        User u = createUser();
        u.setUsername(username);
        return u;
    }

    public static CafeTable createCafeTable() {
        CafeTable cafeTable = new CafeTable();
        cafeTable.setName(Util.randomString(6));
        return cafeTable;
    }

    public static CafeTable createCafeTable(String tableName) {
        CafeTable cafeTable = createCafeTable();
        cafeTable.setName(tableName);
        return cafeTable;
    }

    public static CafeTable createCafeTable(User user) {
        CafeTable cafeTable = createCafeTable();
        cafeTable.setUser(user);
        return cafeTable;
    }

    public static Order createOrder() {
        Order o = new Order();
        o.setCafeTable(createCafeTable());
        o.setStatus(Status.OPEN);
        return o;
    }

    public static Product createProduct() {
        Product product = new Product();
        product.setName(Util.randomString(6));
        return product;
    }

    public static Product createProduct(String productName) {
        Product product = createProduct();
        product.setName(productName);
        return product;
    }

    public static ProductsInOrder createProductInOrder() {
        ProductsInOrder p = new ProductsInOrder();
        Order o = createOrder();
        o.setCafeTable(createCafeTable());
        p.setOrder(o);
        p.setProduct(createProduct());
        p.setAmount(1);
        return p;
    }

    public static CreateUserRequest createCreateUserRequest() {
        return new CreateUserRequest(Util.randomString(6), Util.randomString(6), email);
    }

    public static UpdateUserRequestBody createUpdateUserRequestBody() {
        UpdateUserRequestBody u = new UpdateUserRequestBody();
        u.setFirstName(Util.randomString(6));
        u.setLastName(Util.randomString(6));
        return u;
    }

    public static UserPublicProfile createUserPublicProfile(Long userId) {
        return new UserPublicProfile() {
            @Override
            public Long getId() {
                return userId;
            }

            @Override
            public String getFirstName() {
                return null;
            }

            @Override
            public String getLastName() {
                return null;
            }

            @Override
            public String getUsername() {
                return Util.randomString(6);
            }

            @Override
            public String getEmail() {
                return email;
            }
        };
    }

    public static SimpleTableProps createSimpleTableProps() {
        return new SimpleTableProps() {
            @Override
            public Long getId() {
                return Util.randomLong();
            }

            @Override
            public String getName() {
                return Util.randomString(4);
            }

            @Override
            public FetchedUser getUser() {
                User u = EntitiesBuilder.createUser();
                return new FetchedUser() {
                    @Override
                    public Long getId() {
                        return u.getId();
                    }

                    @Override
                    public String getUsername() {
                        return u.getUsername();
                    }
                };
            }
        };
    }

    public static OnlyTableProps createOnlyTableProps(Long tableId) {
        String name = Util.randomString(6);
        return new OnlyTableProps() {
            @Override
            public Long getId() {
                return tableId;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    public static TableCreateRequestBody createTableCreateRequestBody() {
        TableCreateRequestBody tr = new TableCreateRequestBody();
        tr.setName(Util.randomString(6));
        return tr;
    }

    public static TableWithOpenOrdersCount createTableWithOpenOrdersCount(Integer orderCount) {
        String tableName = Util.randomString(6);
        return new TableWithOpenOrdersCount() {
            @Override
            public Long getId() {
                return Util.randomLong();
            }

            @Override
            public String getName() {
                return tableName;
            }

            @Override
            public Integer getOrderCount() {
                return orderCount;
            }
        };
    }

    public static SimpleProductProps createSimpleProductProps() {
        return new SimpleProductProps() {
            @Override
            public Long getId() {
                return Util.randomLong();
            }

            @Override
            public String getName() {
                return Util.randomString(6);
            }
        };
    }

    public static CreateProductRequest createProductCreateRequestBody() {
        CreateProductRequest pr = new CreateProductRequest();
        pr.setName(Util.randomString(6));
        return pr;
    }

    public static ProductInOrderReq createProductInOrderReq() {
        ProductInOrderReq pioReq = new ProductInOrderReq();
        pioReq.setAmount(1);
        pioReq.setProductId(Util.randomLong());
        return pioReq;
    }

    public static OrderDetails createOrderDetails() {
        Long tableId = Util.randomLong();
        List<ProductInOrderReq> pios = Arrays.asList(createProductInOrderReq(), createProductInOrderReq());
        return new OrderDetails(tableId, pios, createUser());
    }

    public static UpdateProductInOrderDto createUpdateProductInOrderDto() {
        return new UpdateProductInOrderDto(Util.randomLong(), Util.randomLong(), EntitiesBuilder.createUser(), 1);
    }
}
