ALTER TABLE `conferenceOrganisation`.`events` 
ADD COLUMN `isPublished` TINYINT NOT NULL DEFAULT 1 AFTER `availableSeats`;
