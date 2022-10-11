
DROP DATABASE IF EXISTS `mental_health_assist`;

CREATE DATABASE `mental_health_assist`;

USE `mental_health_assist`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `authorities`;

CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
  
  DROP TABLE IF EXISTS `users`;
  
 CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*  BCRYPT PASSWORD is currently 'Test123' */
INSERT INTO `users` VALUE ('TestAdmin', '{bcrypt}$2a$12$MrZIbGAdbth0Vm/YmQcRCuGXOMMtUd8IbVje18xrqRzifU54OIfyq', 1);
INSERT INTO `authorities` VALUE ('TestAdmin', "ROLE_ADMIN");


  DROP TABLE IF EXISTS `health_condition`;
  
  CREATE TABLE `health_condition` (
 `id` int NOT NULL AUTO_INCREMENT, 
  
  `condition_name` varchar(45) NOT NULL,
  
  /* UPDATE TO APPROPRIATE TEXT FOR DESCRIPTION */

  `condition_description` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT UC_Condition UNIQUE (`condition_name`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
  
  




DROP TABLE IF EXISTS `symptom`;

CREATE TABLE `symptom` (
 `id` int NOT NULL AUTO_INCREMENT, 
  `symptom_name` varchar(45) NOT NULL,
  `symptom_description` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT UC_Symptom UNIQUE (`symptom_name`) 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




DROP TABLE IF EXISTS `treatment`;

CREATE TABLE `treatment` (
  `id` int NOT NULL AUTO_INCREMENT, 	
  `treatment_name` varchar(45) NOT NULL,
  `treatment_description` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT UC_Treatment UNIQUE (`treatment_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `condition_treatment`;


CREATE TABLE `condition_treatment` (
  `condition_id` int NOT NULL,
  `treatment_id` int NOT NULL,
  PRIMARY KEY (`condition_id`,`treatment_id`),
  KEY `treat_cond_fk` (`treatment_id`),
  CONSTRAINT `cond_tech_fk` FOREIGN KEY (`condition_id`) REFERENCES `health_condition` (`id`),
  CONSTRAINT `treat_cond_fk` FOREIGN KEY (`treatment_id`) REFERENCES `treatment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `condition_symptom`;

CREATE TABLE `condition_symptom` (
  `condition_id` int NOT NULL,
  `symptom_id` int NOT NULL,
  PRIMARY KEY (`condition_id`,`symptom_id`),
  KEY `sym_cond_fk` (`symptom_id`),
  CONSTRAINT `cond_sym_fk` FOREIGN KEY (`condition_id`) REFERENCES `health_condition` (`id`),
  CONSTRAINT `sym_cond_fk` FOREIGN KEY (`symptom_id`) REFERENCES `symptom` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

  

  SET FOREIGN_KEY_CHECKS =1;


INSERT INTO `health_condition`
 VALUE (0, "Condition Test 1 Name", "Condition Test 1 Description");
 
 INSERT INTO `health_condition`
 VALUE (0, "Condition Test 2 Name", "Condition Test 2 Description");
 
 INSERT INTO `health_condition`
 VALUE (0, "Condition Test 3 Name", "Condition Test 3 Description");
 
 INSERT INTO `symptom`
 VALUE (0, "symptom Test 1 Name", "symptomTest 1 Description");
 
 INSERT INTO `symptom`
 VALUE (0, "symptom Test 2 Name", "symptom Test 2 Description");
 
 INSERT INTO `symptom`
 VALUE (0, "symptom Test 3 Name", "symptom Test 3 Description");
 
 INSERT INTO `treatment`
 VALUE (0, "treatment Test 1 Name", "treatment Test 1 Description");
 
 INSERT INTO `treatment`
 VALUE (0, "treatment Test 2 Name", "treatment Test 2 Description");
 
 INSERT INTO `treatment`
 VALUE (0, "treatment Test 3 Name", "treatment Test 3 Description");
 
 
 /*
INSERT INTO `condition_symptom` VALUE ("1", "2");

INSERT INTO `condition_treatment` VALUE ("1", "2");
*/
  