create schema `tlt_console`;

CREATE TABLE tlt_console.units (
`unit_id` bigint NOT NULL AUTO_INCREMENT,
`name` varchar(255) ,
`description` varchar(500) ,
`parent_unit_id` bigint ,
`unit_type` varchar(255) NOT NULL,
`active` varchar(1) default 'Y' NOT NULL,
`update_date` datetime NOT NULL default NOW(),
`update_user` bigint NOT NULL default 1,
PRIMARY KEY (`unit_id`),
CONSTRAINT name UNIQUE (name)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE tlt_console.client_detail (
`client_id` bigint NOT NULL AUTO_INCREMENT,
`name` varchar(255) NOT NULL,
`address` varchar(500) ,
`num_of_people` bigint ,
`id_proof_type` varchar(255) ,
`id_proof_unique_number` varchar(255) ,
`additional_request` varchar(500) ,
`unit_id` bigint NOT NULL,
`booked_date` datetime ,
`update_date` datetime NOT NULL default NOW(),
`update_user` bigint NOT NULL default 1,
PRIMARY KEY (`client_id`),
UNIQUE `unique_client`(`id_proof_unique_number`, `booked_date`, `unit_id`),
CONSTRAINT `fk_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `units` (`unit_id`)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE tlt_console.services (
`service_id` bigint NOT NULL AUTO_INCREMENT,
`name` varchar(255) ,
`description` varchar(500) ,
`active` varchar(1) default 'Y' NOT NULL,
`update_date` datetime NOT NULL default NOW(),
`update_user` bigint NOT NULL default 1,
PRIMARY KEY (`service_id`),
CONSTRAINT name UNIQUE (name)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE tlt_console.client_expense (
`expense_id` bigint NOT NULL AUTO_INCREMENT,
`service_id`  bigint NOT NULL,
`client_id` bigint NOT NULL ,
`cash_in` bigint ,
`cash_out` bigint ,
`description` varchar(500) ,
`update_date` datetime NOT NULL default NOW(),
`update_user` bigint NOT NULL default 1,
PRIMARY KEY (`expense_id`),
CONSTRAINT `fk_service_id` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`),
CONSTRAINT `fk_client_id` FOREIGN KEY (`client_id`) REFERENCES `client_detail` (`client_id`)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE tlt_console.client_check_in_calendar (
`id` bigint NOT NULL AUTO_INCREMENT,
`unit_id`  bigint NOT NULL,
`client_id` bigint NOT NULL ,
`booked_date` date ,
`description` varchar(500) ,
`update_date` datetime NOT NULL default NOW(),
`update_user` bigint NOT NULL default 1,
PRIMARY KEY (`id`),
CONSTRAINT `fk_check_in_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `units` (`unit_id`),
CONSTRAINT `fk_check_in_client_id` FOREIGN KEY (`client_id`) REFERENCES `client_detail` (`client_id`)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
