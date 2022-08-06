delete from RECIPE_INGREDIENTS;
delete from RECIPE;

insert into RECIPE(ID, RECIPE_NAME, IS_VEGETARIAN, NO_OF_SERVINGS, INSTRUCTION) values
(80, 'chicken biriyani', false, 4, 'put chicken and rice together'),
(81, 'chocolate muffin', true, 2, 'put chocolate and sugar together'),
(82, 'chocolate cake', true, 1, 'put chocolate and baking soda together in oven');


insert into RECIPE_INGREDIENTS (ID, INGREDIENTS) values
(80, 'chicken'), (80, 'rice'), (81, 'chocolate'), (81, 'sugar'),
(82, 'chocolate'), (82, 'baking soda'), (82, 'milk');

