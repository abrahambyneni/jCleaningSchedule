/*
 * jCleaningSchedule - program for printing house cleaning schedules
 * Copyright (C) 2013  Martin Mare¨ <mmrmartin[at]gmail[dot]com>
 *
 * This file is part of jCleaningSchedule.
 *
 * jCleaningSchedule is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jCleaningSchedule is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jCleaningSchedule.  If not, see <http://www.gnu.org/licenses/>.
 */

#include  <stdlib.h>
#include  <stdio.h>
#include  <windows.h>

int main () {
	ShellExecute(NULL, "open", "java", "-jar jCleaningSchedule.jar", NULL, SW_HIDE);
	
	return 0;	
}
