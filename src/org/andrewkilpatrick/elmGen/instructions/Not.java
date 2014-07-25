/* ElmGen - DSP Development Tool
 * Copyright (C)2011 - Andrew Kilpatrick
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 */
package org.andrewkilpatrick.elmGen.instructions;

import org.andrewkilpatrick.elmGen.simulator.SimulatorState;

/**
 * This class represents the NOT instruction.
 * 
 * @author andrew
 */
public class Not extends Instruction {
	
	/**
	 * Negate all bit in ACC.
	 */
	public Not() {
		// nothing to do here
	}
	
	@Override
	public int getHexWord() {
		return ((int)0xffffff << 8) | 0x10;
	}

	@Override
	public String getInstructionString() {
		return "Not()";
	}
	
	@Override
	public void simulate(SimulatorState state) {
		state.getACC().not();
	}
}