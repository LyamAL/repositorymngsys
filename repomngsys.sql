drop database if exists `repomngsys`;
create database `repomngsys`;
use `repomngsys`;

/*验证授权管理*/
create table `user`
(
    username char(10) primary key,
    phone    int(11),
    password char(10) not null,
    role     char(12) default 'USER'
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;

#商品信息表
create table good
(
    #编号
    id     int(10) primary key,
    #货源地
    origin char(30),
    #名称
    name   char(30),
    #价格
    price  decimal(8, 2),
    #单位
    unit   char(4)
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;

/* 库寸信息 */
create table storage
(
    gid       int(10) references good (id),
    #数量
    count     mediumint check ( count >= 0 ),
    #合格品
    qualified mediumint check ( qualified >= 0 and qualified <= count),
    #库区号
    repoId    int(10) not null references repo (id)
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;

/*发货单*/
create table `deliverysheet`
(
    id          binary(16) primary key,
    #创建时间(销售时间)
    createtime  datetime,
    #完成时间
    closetime   datetime,

    #库区
    repoId      int(10) not null references repo (id),
    #0:订单创建 1:拣货完成  2:关闭回传 3: 订单取消
    status      int(1) default 0,

    #联系人
    contact     char(10) references user (username),
    #送货地点
    destination char(30),
    #备注
    note        char(255),
    constraint chk_status_delivery_val check ( status in (0, 1, 2, 3) )
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;

/*发货内容*/
create table `deliveryGoods`
(
    #货物ID
    gid        int(10) references good (id),
    #数量
    count      smallint check ( count > 0 ),
    #单价
    price      decimal(8, 2),
    #总价
    sum        decimal(8, 2),
    #备注
    note       char(255),
    #出库单号
    deliveryId binary(16) references deliverysheet (id)
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;
create table `entryGoods`
(
    #货物ID
    gid       int(10) references good (id),
    #数量
    count     smallint check ( count > 0 ),
    #单价
    price     decimal(8, 2),
    #总价
    sum       decimal(8, 2),
    #合格品
    qualified smallint check ( qualified >= 0 and qualified <= count ),
    #入库单号
    entryId   binary(16) references `entrysheet` (id)
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;
create table `entrysheet`
(
    id         binary(16) primary key,
    #创建时间
    createtime datetime,
    #完成时间
    closetime  datetime,
    #库区
    repoId     int(10)  not null references repo (id),
    #联系人
    contact    char(10) references user (username),
    #0:订单创建 1:部分完成  2:入库完成 3: 订单取消
    status     int(1) default 0,
    #备注
    note       char(255),
    #供应商
    producer   char(20) not null,
    constraint chk_status_entry_val check ( status in (0, 1, 2, 3) )
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;
create table `repo`
(
    #库区号
    id       int(10) primary key auto_increment,
    #容量
    capacity mediumint default 0,
    #已用容量
    used     mediumint default 0,
    #货区地点
    position char(30)
) DEFAULT CHARSET = utf8
  ENGINE = InnoDB;


insert into user
values ('admin', 'admin', '17754833705', 'ADMIN');
insert into user
values ('王博', '000000', '17754831111', 'USER');
insert into user
values ('赵青', '000000', '17712129999', 'USER');
insert into user
values ('大乔', '000000', '17712123456', 'USER');
insert into user
values ('公孙离', '000000', '13685503400', 'USER');
insert into user
values ('诸葛亮', '000000', '13422118763', 'USER');
insert into user
values ('孙悟空', '000000', '19778347299', 'USER');
insert into user
values ('杨戬', '000000', '18834887644', 'USER');
insert into user
values ('赵云', '000000', '13799873321', 'USER');
insert into repo
values (1, 10000, 200, '安徽芜湖');
insert into repo
values (2, 5000, 300, '安徽宣城');
insert into repo
values (3, 10000, 221, '安徽滁州');
insert into repo
values (4, 40000, 2100, '北京朝阳区');
insert into repo
values (5, 10000, 300, '上海');
insert into repo
values (6, 7000, 90, '安徽六安');
insert into repo
values (7, 30000, 490, '江苏南京');
insert into good
values (1, '江苏南京', '雨花石', 228, '个');
insert into good
values (2, '安徽滁州', '秋冬百搭棉服', 160, '件');
insert into good
values (3, '安徽芜湖', '傻子瓜子', 19.9, '500g');
insert into good
values (4, '北京', '北京烤鸭', 89, '只');
insert into good
values (5, '上海', '梅花糕', 8, '个');
insert into good
values (6, '重庆', '晶体管', 2, '个');
insert into good
values (7, '安徽铜陵', '烤火箱', 150, '个');

insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 7, '诸葛亮', 0, '', '南京雨花石有限公司');
insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 2, '赵云', 1, '', '三只松鼠');
insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 3, '孙悟空', 0, '', '百武西');
insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 5, '大乔', 3, '', '雅诗兰黛');
insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 4, '公孙离', 0, '', 'OLAY');
insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 1, '王博', 0, '', '芯样科技电子企业店');
insert into entrysheet
values (unhex(replace(uuid(), '-', '')), now(), 6, '赵青', 0, '', '百草味食品电商');

insert into storage
values (1, 100, 97, 7);
insert into storage
values (2, 100, 100, 3);
insert into storage
values (7, 121, 100, 3);
insert into storage
values (3, 200, 180, 1);
insert into storage
values (4, 2000, 1844, 4);
insert into storage
values (5, 300, 284, 5);
insert into storage
values (6, 100, 97, 4);
insert into storage
values (7, 300, 297, 2);
insert into storage
values (3, 90, 90, 6);
insert into storage
values (2, 190, 188, 7);

insert into deliverysheet
values (unhex(replace(uuid(), '-', '')), now(), 1, 1, '王博', '安徽滁州', '');
insert into deliverysheet
values (unhex(replace(uuid(), '-', '')), now(), 2, 2, '赵云', '浙江宁波', '');
insert into deliverysheet
values (unhex(replace(uuid(), '-', '')), now(), 3, 0, '孙悟空', '云南大理', '');
insert into deliverysheet
values (unhex(replace(uuid(), '-', '')), now(), 4, 1, '公孙离', '北京海淀区', '');
insert into deliverysheet
values (unhex(replace(uuid(), '-', '')), now(), 5, 0, '大乔', '海南', '');
insert into deliverysheet
values (unhex(replace(uuid(), '-', '')), now(), 6, 0, '赵青', '广州东莞', '');

# insert into entryGoods values (1,10,100,1000,9,);