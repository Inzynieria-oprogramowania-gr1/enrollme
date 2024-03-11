-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb3 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Students` (
  `idStudents` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(60) NULL DEFAULT NULL,
  PRIMARY KEY (`idStudents`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`Timetable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Timetable` (
  `idTimetable` INT NOT NULL AUTO_INCREMENT,
  `start_time` TIME NULL DEFAULT NULL,
  `end_time` TIME NULL DEFAULT NULL,
  `isSelectd` BIT(1) NULL DEFAULT b'0',
  `weekDay` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idTimetable`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`Preferences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Preferences` (
  `Students_idStudents` INT NOT NULL,
  `Timetable_idTimetable` INT NOT NULL,
  INDEX `fk_Preferences_Students1_idx` (`Students_idStudents` ASC) VISIBLE,
  INDEX `fk_Preferences_Timetable1_idx` (`Timetable_idTimetable` ASC) VISIBLE,
  CONSTRAINT `fk_Preferences_Students1`
    FOREIGN KEY (`Students_idStudents`)
    REFERENCES `mydb`.`Students` (`idStudents`),
  CONSTRAINT `fk_Preferences_Timetable1`
    FOREIGN KEY (`Timetable_idTimetable`)
    REFERENCES `mydb`.`Timetable` (`idTimetable`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`Results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Results` (
  `Students_idStudents` INT NOT NULL,
  `Timetable_idTimetable` INT NOT NULL,
  INDEX `fk_Results_Students_idx` (`Students_idStudents` ASC) VISIBLE,
  INDEX `fk_Results_Timetable1_idx` (`Timetable_idTimetable` ASC) VISIBLE,
  CONSTRAINT `fk_Results_Students`
    FOREIGN KEY (`Students_idStudents`)
    REFERENCES `mydb`.`Students` (`idStudents`),
  CONSTRAINT `fk_Results_Timetable1`
    FOREIGN KEY (`Timetable_idTimetable`)
    REFERENCES `mydb`.`Timetable` (`idTimetable`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;
