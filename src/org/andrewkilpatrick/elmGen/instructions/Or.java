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
 * This class represents the OR instruction.
 * 
 * @author andrew
 */
public class Or extends Instruction {
	final int mask;
	
	/**
	 * ORs the the ACC with mask.
	 * 
	 * @param mask the 24 bit mask
	 */
	public Or(int mask) {
		this.mask = mask & 0xffffff;
	}
	
	@Override
	public int getHexWord() {
		return (mask << 8) | 0x0f;
	}

	@Override
	public String getInstructionString() {
		return "Or(" + String.format("%08X", mask) + ")";
	}

	@Override
	public void simulate(SimulatorState state) {
		state.getACC().or(mask);
	}
}
