/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.common.volume.base;

import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;

import com.google.common.base.Objects;

public class Volume {
	protected Fraction fraction = Fraction.empty();

	protected Fraction size = new Fraction(Integer.MAX_VALUE, 1);

	/**
	 * Instantiates a Volume with an empty fraction.
	 */
	public Volume() {}

	/**
	 * Instantiates a Volume with a specified fraction.
	 */
	public Volume(Fraction fraction) {
		this.fraction = fraction;
	}

	public boolean isFull() {
		return getFraction().equals(getSize());
	}

	public boolean isEmpty() {
		return this.getFraction().equals(Fraction.empty());
	}

	public Fraction getFraction() {
		return this.fraction;
	}

	public void setFraction(Fraction fraction) {
		this.fraction = fraction;
	}

	/**
	 * Serializes this Volume and its properties into a tag.
	 *
	 * @return a tag
	 */
	public CompoundTag toTag(CompoundTag tag) {
		// TODO: Null checks.

		tag.put("fraction", this.fraction.toTag(new CompoundTag()));
		tag.put("size", this.size.toTag(new CompoundTag()));

		return tag;
	}

	/**
	 * Takes a Volume out of this Volume.
	 */
	public <T extends Volume> T extractVolume(Fraction taken) {
		T volume = (T) new Volume();
		pushVolume(volume, taken);
		return volume;
	}

	public <T extends Volume> T insertVolume(Fraction fraction) {
		Volume volume = new Volume(fraction);

		this.pullVolume(volume, fraction);

		return (T) volume;
	}

	/**
	 * Pull fluids from a Volume into this Volume. If the Volume's fractional available is smaller than pulled, ask for
	 * the minimum. If not, ask for the minimum between requested size and available for pulling into this.
	 */
	public <T extends Volume> void pullVolume(T target, Fraction pulled) {
		if (target.fraction.isSmallerOrEqualThan(Fraction.empty()))
			return;

		Fraction available = Fraction.subtract(this.size, this.fraction);

		pulled = Fraction.min(pulled, available);

		if (target.fraction.isSmallerThan(pulled)) { // If target has less than required.
			setFraction(Fraction.add(getFraction(), target.fraction));
			target.setFraction(Fraction.subtract(target.fraction, target.fraction));

			setFraction(Fraction.simplify(getFraction()));
			target.setFraction(Fraction.simplify(target.getFraction()));
		} else { // If target has more than or equal to required.
			target.setFraction(Fraction.subtract(target.fraction, pulled));
			setFraction(Fraction.add(getFraction(), pulled));

			target.setFraction(Fraction.simplify(target.getFraction()));
			setFraction(Fraction.simplify(getFraction()));
		}
	}

	/**
	 * Push fluids from this Volume into a Volume. If the Volume's fractional available is smaller than pushed, ask for
	 * the minimum. If not, ask for the minimum between requested size and available for pushing into target.
	 */
	public <T extends Volume> void pushVolume(T target, Fraction pushed) {
		if (fraction.isSmallerOrEqualThan(Fraction.empty()))
			return;

		Fraction available = Fraction.subtract(target.size, target.fraction);

		pushed = Fraction.min(pushed, available);

		if (fraction.isSmallerThan(pushed)) { // If target has less than required.
			target.setFraction(Fraction.add(target.getFraction(), fraction));
			setFraction(Fraction.subtract(fraction, fraction));

			target.setFraction(Fraction.simplify(target.getFraction()));
			setFraction(Fraction.simplify(getFraction()));
		} else { // If target has more than or equal to required.
			target.setFraction(Fraction.add(target.getFraction(), pushed));
			setFraction(Fraction.subtract(fraction, pushed));

			target.setFraction(Fraction.simplify(target.getFraction()));
			setFraction(Fraction.simplify(getFraction()));
		}
	}

	public Fraction getSize() {
		return this.size;
	}

	public void setSize(Fraction size) {
		this.size = size;
	}

	public Fraction getAvailable() {
		return Fraction.subtract(getSize(), getFraction());
	}

	public boolean hasAvailable(Fraction fraction) {
		Fraction available = getAvailable();
		return available.equals(fraction) || available.isBiggerThan(fraction);
	}

	public boolean hasStored(Fraction fraction) {
		return this.fraction.isBiggerOrEqualThan(fraction);
	}

	/**
	 * Fraction comparison method.
	 */
	public <T extends Volume> boolean isSmallerThan(T volume) {
		return !this.isBiggerThan(volume);
	}

	/**
	 * Fraction comparison method.
	 */
	public <T extends Volume> boolean isBiggerThan(T volume) {
		return fraction.isBiggerThan(volume.fraction);
	}

	/**
	 * Fraction comparison method.
	 */
	public <T extends Volume> boolean isSmallerOrEqualThan(T volume) {
		return isSmallerThan(volume) || equals(volume);
	}

	/**
	 * Fraction comparison method.
	 */
	public <T extends Volume> boolean isBiggerOrEqualThan(T volume) {
		return isBiggerThan(volume) || equals(volume);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.fraction);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof Volume))
			return false;

		Volume volume = (Volume) object;

		return Objects.equal(this.fraction, volume.fraction);
	}
}
