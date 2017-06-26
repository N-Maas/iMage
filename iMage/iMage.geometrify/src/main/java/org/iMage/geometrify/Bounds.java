package org.iMage.geometrify;

import java.awt.Point;
import java.util.Random;

public abstract class Bounds {
	public static final Bounds NO_BOUNDS = new Bounds() {
		@Override
		public RandomPointGenerator bind(int x, int y, int width, int height) {
			return new RandomPointGenerator(x, y, width, height);
		}

	};

	public static final Bounds RANDOM_SCALING_BOUNDS = new Bounds() {
		@Override
		public RandomPointGenerator bind(int x, int y, int width, int height) {
			RandomPointGenerator generator = new RandomPointGenerator(width, height);
			Point a = generator.nextPoint();
			Point b;
			do {
				b = generator.nextPoint();
			} while (Math.abs(a.x - b.x + 1) < 2 || Math.abs(a.y - b.y + 1) < 2);
			return new RandomPointGenerator(x + Math.min(a.x, b.x), y + Math.min(a.y, b.y), Math.abs(a.x - b.x + 1),
					Math.abs(a.y - b.y + 1));
		}
	};

	public static final Bounds RANDOM_LINEAR_BOUNDS = new Bounds() {
		@Override
		public RandomPointGenerator bind(int x, int y, int width, int height) {
			Random r = new Random();
			int genWidth = 2 + r.nextInt(width - 2);
			int genHeight = 2 + r.nextInt(height - 2);
			RandomPointGenerator generator = new RandomPointGenerator(width - genWidth, height - genHeight);
			Point p = generator.nextPoint();
			return new RandomPointGenerator(x + p.x, y + p.y, genWidth, genHeight);
		}
	};

	private Bounds() {
	}

	public RandomPointGenerator bind(int width, int height) {
		return this.bind(0, 0, width, height);
	}

	public abstract RandomPointGenerator bind(int x, int y, int width, int height);

	public static Bounds noBounds() {
		return NO_BOUNDS;
	}

	public static Bounds randomBounds() {
		return RANDOM_SCALING_BOUNDS;
	}

	public static Bounds randomBounds(boolean scaling) {
		return scaling ? RANDOM_SCALING_BOUNDS : RANDOM_LINEAR_BOUNDS;
	}

	public static Bounds fixedBounds(int size) {
		return fixedBounds(size, size);
	}

	public static Bounds fixedBounds(int width, int height) {
		if (width < 2 || height < 2) {
			throw new IllegalArgumentException("Illegal bounds.");
		}
		return new Bounds() {
			@Override
			public RandomPointGenerator bind(int x, int y, int widthX, int heightY) {
				int genHeight = Math.min(heightY, height);
				int genWidth = Math.min(widthX, width);
				RandomPointGenerator generator = new RandomPointGenerator(x, y, widthX - genWidth + 1,
						heightY - genHeight + 1);
				Point p = generator.nextPoint();
				return new RandomPointGenerator(p.x, p.y, genWidth, genHeight);
			}
		};
	}
}
