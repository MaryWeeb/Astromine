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

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashSet;
import java.util.Set;

public class AstromineDimensions {
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();

	public static final RegistryKey<DimensionOptions> EARTH_SPACE_OPTIONS = register(Registry.DIMENSION_OPTIONS, AstromineCommon.identifier("earth_space"));
	public static final RegistryKey<DimensionType> EARTH_SPACE_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("earth_space"));

	public static final RegistryKey<DimensionOptions> MOON_OPTIONS = register(Registry.DIMENSION_OPTIONS, AstromineCommon.identifier("moon"));
	public static final RegistryKey<DimensionType> MOON_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("moon"));

	public static final RegistryKey<DimensionType> MARS_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("mars"));

	public static final RegistryKey<DimensionType> VULCAN_REGISTRY_KEY = register(Registry.DIMENSION_TYPE_KEY, AstromineCommon.identifier("vulcan"));

	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		RegistryKey<T> key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}

	public static void initialize() {
		// Unused.
	}
}
