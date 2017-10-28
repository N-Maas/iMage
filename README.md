# iMage
Result of tasks for SWT (software engineering) over the 2.  semester

The project is built with maven, thus maven is needed to build and execute it.
The main application is iMage.iLlustrate, which is a GUI program for the application of a special kind of image filter.
You can load images and apply filters with different settings, also a preview is shown.
All filters are reconstructing the image by using geometrical primitives (such as triangles, squares, stars...).
"Iterations" defines the number of iterations for building the new image, where one primitive is added at each iteration.
This primitive is chosen as the best fitting primitive of a number of randomly generated primitives,
where the number of generated primitives is defined by "Samples".
