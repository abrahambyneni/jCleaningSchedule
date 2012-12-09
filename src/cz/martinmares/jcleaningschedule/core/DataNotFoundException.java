/*
 * jCleaningSchedule - program for printing house cleaning schedules
 * Copyright (C) 2012  Martin Mareš <mmrmartin[at]gmail[dot]com>
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

package cz.martinmares.jcleaningschedule.core;

import java.io.IOException;

/**
 * The <code>JCleaningScheduleData</code> error class.
 * This error is throwen if application cannot find settings file.
 * @author Martin Mareš
 */
public class DataNotFoundException extends IOException {

    /**
     * Default public costructor.
     */
    public DataNotFoundException(String e) {
        super(e);
    }
       
}
