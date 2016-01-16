CREATE TABLE `joueur` (
  `id` int(11) NOT NULL PRIMARY KEY ,
  `pseudo` VARCHAR(255) NOT NULL,
  `motdepasse` VARCHAR(255) DEFAULT NULL,
  `nb_partie_gagne` INT NOT NULL,
  `nb_partie_joue` INT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;