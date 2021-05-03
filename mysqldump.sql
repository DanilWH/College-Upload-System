-- MySQL dump 10.13  Distrib 8.0.23, for Linux (x86_64)
--
-- Host: localhost    Database: college_upload_system_db
-- ------------------------------------------------------
-- Server version	8.0.23-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `college_groups`
--

DROP TABLE IF EXISTS `college_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `college_groups` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `college_groups`
--

LOCK TABLES `college_groups` WRITE;
/*!40000 ALTER TABLE `college_groups` DISABLE KEYS */;
INSERT INTO `college_groups` VALUES (24,'ИС-21'),(41,'new'),(67,'Comostas'),(93,'IS-41'),(94,'IS-41'),(99,'IS-51'),(101,'sd'),(128,'Петухи'),(132,'Петухи'),(137,'СССР');
/*!40000 ALTER TABLE `college_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','Init','SQL','V1__Init.sql',752513873,'admin','2021-04-03 16:05:25',125,1),(2,'2','Add admin','SQL','V2__Add_admin.sql',1343381133,'admin','2021-04-04 13:26:14',13,1),(3,'3','Add students','SQL','V3__Add_students.sql',-1974979457,'admin','2021-04-04 13:32:29',167,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (142);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students_results`
--

DROP TABLE IF EXISTS `students_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students_results` (
  `id` bigint NOT NULL,
  `date_time` datetime(6) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `task_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `filepath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt490werh2cot9d3mmp14gwhrr` (`task_id`),
  KEY `FK4g1h5dtlug7lcrh96obgvx14` (`user_id`),
  CONSTRAINT `FK4g1h5dtlug7lcrh96obgvx14` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt490werh2cot9d3mmp14gwhrr` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students_results`
--

LOCK TABLES `students_results` WRITE;
/*!40000 ALTER TABLE `students_results` DISABLE KEYS */;
INSERT INTO `students_results` VALUES (37,'2021-04-21 11:01:35.170549','ЛомакинДанил_fdecf812-19fc-4714-9dbb-b0f4b5ec7468_general.html',25,11,'ИС-21/1C/'),(38,'2021-04-12 22:33:30.618639','ЛомакинДанил_d0c4cb32-54dd-42a6-bc9a-91b3e91ad61a_max7219test_scrol.ino',27,11,'/ИС-21/Методичка/'),(39,'2021-04-12 22:50:07.711837','ТалыповВадим_eaa2a15f-1e34-410f-bcb0-80232f6e0f9f_Сборник формул по физике ЕГЭ 2020.pdf',25,20,'/ИС-21/1C/'),(40,'2021-04-14 00:20:26.633680','БояршиновНикита_15551983-6376-4219-a821-d0c9e1677166_max7219test_scrol.ino',25,2,'ИС-21/1C/'),(102,'2021-04-16 15:37:31.051886','AnatolevichIgor_53226415-6988-4fd1-a2b0-26e82eeb80ed_login.html',25,0,'ИС-21/1C/'),(104,'2021-04-18 00:22:14.843991','ЛомакинДанил_ccaf4516-b610-47f5-be05-9cd639ac6ab2_login.html',103,11,'ИС-21/Практика/'),(105,'2021-04-18 00:24:33.068796','БояршиновНикита_b040e534-24b6-45a1-bfc8-cd8f5fc44dc1_students.html',103,2,'ИС-21/Практика/'),(106,'2021-04-18 00:24:47.029508','БояршиновНикита_e42415ad-9e58-458d-82d2-eb888a9ab112_general.html',27,2,'ИС-21/Методичка/'),(108,'2021-04-18 00:53:33.019031','ЛомакинДанил_c526ab08-612b-4132-84d4-00ed0ac75372_general.html',107,11,'ИС-21//'),(114,'2021-04-21 10:33:33.511985','ЛомакинДанил_49e36bd6-330e-4d61-aa90-1e9de1faf0c0_general.html',112,11,'ИС-21/New task/'),(117,'2021-04-21 10:43:09.199348','ЛомакинДанил_74593ce4-79fa-4c1d-8e54-46bf7a6abf92_general.html',116,11,'IS-41/comastas/'),(118,'2021-04-21 11:01:44.542021','ЛомакинДанил_be6d2405-e141-4105-b7ab-99be2604d6bf_Main.java',113,11,'ИС-21/this/'),(119,'2021-04-22 08:29:42.926581','ГорбачёваДиана_c0b9946b-b79c-420e-8c2e-228915d0e826_sql.sql',25,6,'ИС-21/1C/'),(120,'2021-04-22 08:29:48.985734','ГорбачёваДиана_597f0322-de48-4c65-80bb-88c19b5e1fa1_V3_Add_students.sql',107,6,'ИС-21/Some task/'),(121,'2021-04-22 09:09:22.856142','ГорбачёваДиана_fde15812-654c-44f8-98bb-e7bfe4ef8000_Main.class',103,6,'ИС-21/Практика/'),(123,'2021-04-22 16:28:48.918549','ЛомакинДанил_2480d256-7290-468e-bef3-386d3062c69f_sql.sql',122,11,'ИС-21/dd/'),(127,'2021-04-27 09:03:53.271802','ОрловАлександр_e909e009-2627-4d32-b851-a5c28fc85849_sql.sql',27,15,'ИС-21/Методичка/');
/*!40000 ALTER TABLE `students_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `group_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6f7xse7bt00ocfqshrg58nmoe` (`group_id`),
  CONSTRAINT `FK6f7xse7bt00ocfqshrg58nmoe` FOREIGN KEY (`group_id`) REFERENCES `college_groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (25,'1C',24),(27,'Методичка',24),(103,'Практика',24),(107,'Some task',24),(112,'New task',24),(113,'this',24),(115,'comostas task',67),(116,'comastas',94),(122,'dd',24),(124,'null',24),(125,'test',24),(136,'Сделать Петухов',132);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `user_roles` varchar(255) DEFAULT NULL,
  KEY `user_role_fk` (`user_id`),
  CONSTRAINT `user_role_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (0,'ADMIN'),(1,'STUDENT'),(3,'STUDENT'),(4,'STUDENT'),(5,'STUDENT'),(6,'STUDENT'),(7,'STUDENT'),(8,'STUDENT'),(9,'STUDENT'),(10,'STUDENT'),(12,'STUDENT'),(13,'STUDENT'),(14,'STUDENT'),(15,'STUDENT'),(16,'STUDENT'),(17,'STUDENT'),(18,'STUDENT'),(19,'STUDENT'),(20,'STUDENT'),(21,'STUDENT'),(22,'STUDENT'),(23,'STUDENT'),(95,'STUDENT'),(96,'STUDENT'),(97,'STUDENT'),(98,'STUDENT'),(100,'STUDENT'),(2,'STUDENT'),(11,'STUDENT'),(133,'STUDENT'),(134,'STUDENT'),(135,'STUDENT'),(138,'STUDENT'),(139,'STUDENT'),(140,'STUDENT'),(141,'STUDENT');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `group_id` bigint DEFAULT NULL,
  `creation_time` datetime(6) DEFAULT NULL,
  `father_name` varchar(255) DEFAULT NULL,
  `password_change_time` datetime(6) DEFAULT NULL,
  `password_changer_id` bigint DEFAULT NULL,
  `user_creator_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK65diy3dd6ypgq49e9jqwkdrp2` (`group_id`),
  KEY `FKn4j7t5sv8ye9a10rqwmiihdjy` (`password_changer_id`),
  KEY `FKrhnxbje0hk94m5syfsdo8gu7e` (`user_creator_id`),
  CONSTRAINT `FK65diy3dd6ypgq49e9jqwkdrp2` FOREIGN KEY (`group_id`) REFERENCES `college_groups` (`id`),
  CONSTRAINT `FKn4j7t5sv8ye9a10rqwmiihdjy` FOREIGN KEY (`password_changer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrhnxbje0hk94m5syfsdo8gu7e` FOREIGN KEY (`user_creator_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (0,'Igor','Anatolevich','admin','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',NULL,NULL,NULL,NULL,NULL,NULL),(1,'Никита','Борцов','БорцовНикита','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(2,'Никита','Бояршинов','nokia','$2a$12$1F.2zb.58PwAXbPXZKueXu.w82WkKJpEaPmzxDmbdAjD1ErkISIGC',24,NULL,NULL,NULL,NULL,NULL),(3,'Тимофей','Власов','ВласовТимофей','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(4,'Даниил','Гайфуллин','ГайфуллинДаниил','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(5,'Кирилл','Глухих','ГлухихКирилл','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(6,'Диана','Горбачёва','ГорбачёваДиана','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(7,'Николай','Дегонский','ДегонскийНиколай','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(8,'Тимур','Зайдулин','ЗайдулинТимур','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(9,'Игорь','Копытов','КопытовИгорь','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(10,'Денис','Кулюкин','КулюкинДенис','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(11,'Данил','Ломакин','Danil','$2a$12$8zAFPvy5Qfa0i6CfeXhKZeqw9C6mUZX1oZHzFfxiUt1hra9dMzZLq',24,NULL,NULL,NULL,NULL,NULL),(12,'Даниил','Маланчуков','МаланчуковДаниил','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(13,'Игроь','Малофеев','МалофеевИгроь','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(14,'Оксана','Муллаярова','МуллаяроваОксана','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(15,'Александр','Орлов','ОрловАлександр','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(16,'Олег','Пигалов','ПигаловОлег','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(17,'Данила','Ролдугин','РолдугинДанила','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(18,'Августина','Смольникова','СмольниковаАвгустина','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(19,'Михаил','Собакин','СобакинМихаил','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(20,'Вадим','Талыпов','ТалыповВадим','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(21,'Михаил','Уклов','УкловМихаил','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(22,'Никита','Четвериков','ЧетвериковНикита','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(23,'Виктор','Ширягин','ШирягинВиктор','$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW',24,NULL,NULL,NULL,NULL,NULL),(95,'asfd','adsf','asdf','asdf',94,NULL,NULL,NULL,NULL,NULL),(96,'ff','ff','ff','ff',94,NULL,NULL,NULL,NULL,NULL),(97,'','ff','','',94,NULL,NULL,NULL,NULL,NULL),(98,'ff','','','',94,NULL,NULL,NULL,NULL,NULL),(100,'gaga','gaga','gaga','$2a$12$LwJQBwU3i5.46b.MyDefDuwlpM.QOBIZMzf8xVclukvGeCi.rionq',99,NULL,NULL,NULL,NULL,NULL),(133,'Петух','Петухов','Петухов_ПП0','$2a$12$1BmSgU8TtJEvPtixSV5mYuI/G6EwrGaNNVHnt7fsHCHX119gYoQZm',132,'2021-04-29 16:19:00.833058','Петухович','2021-05-03 14:15:06.479809',133,NULL),(134,'Прапорщик','Это','Это_ПП1','$2a$12$Lqjnj5eMdu/XRcTYVqXwo.jQ5B7Dt6hA8X4hI7kyR/fWrNySyKqYS',132,'2021-04-29 16:19:01.143022','Понятие','2021-04-29 16:19:01.143022',NULL,NULL),(135,'Hello','My','My_HF2','$2a$12$vT5TnrTYAYzkfwD/q0TXBuWA4YKMQDzXNmwUriw67B6QzEjfixarW',132,'2021-04-29 16:19:01.449953','Friend','2021-04-29 16:19:01.449953',NULL,NULL),(138,'Ктото','Из','Из_КС0','$2a$12$6GjTSacd/rzJadfId5XkvOVB3GJVlGuGp0CEVPigZ4HLUiI2D//bO',137,'2021-05-03 15:25:49.916345','СССР','2021-05-03 15:25:49.916345',0,0),(139,'Еще','Один','Один_ЕИ1','$2a$12$M.xchQRO5evqv7nnMgFKBu9Jifs/ps6V8Nye0qbjF3/7o0jVpb0Me',137,'2021-05-03 15:25:50.224660','ИзСССР','2021-05-03 15:25:50.224660',0,0),(140,'Октябрёнок','Дворца','Дворца_ОП2','$2a$12$YZYrEs7yUF55jN70eQSZvu9zqscd0yaIZhtyEbhATBNsZLAhgKnC6',137,'2021-05-03 15:25:50.531947','Победы','2021-05-03 15:25:50.531947',0,0),(141,'Колобок','Съел','Съел_КЛ3','$2a$12$umrJGV5aU9aTMmTOFVRBi.Q/HPresSE1nQJ.3/pBpWSi.LQv4XczW',137,'2021-05-03 15:25:50.842257','Лису','2021-05-03 15:25:50.842257',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-03 15:39:31
