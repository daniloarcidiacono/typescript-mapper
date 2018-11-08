/**
 * This file is automatically generated by TypescriptMapper.
 * Do not modify this file -- YOUR CHANGES WILL BE ERASED!
 */
export interface BaseNestedInterfaceDTO<K, V> {
}

export interface BaseDTO<K> {
}

export interface BaseDerivedDTO<K, V> {
	a: K;
	b: V;
}

export interface BaseDerivedStaticDTO<K, V> {
	a: K;
	b: V;
}

export interface BoundedTypesDTO<T extends A, A extends string, B> {
	genericArray: GenericDTO<any>[];
	wildcardExtends: GenericDTO<A>;
	wildcardSuper: GenericDTO<any>;
	wildcard: GenericDTO<any>;
	first: T;
	second: A;
	omittedParams: BoundedTypesDTO<any, any, any>;
	self: BoundedTypesDTO<T, A, B>;
	nestedGenerics: BoundedTypesDTO<T, A, B>[];
	genericList: T[];
	nestedList: T[][];
	map1: { [ index: string ]: BoundedTypesDTO<T, A, B[]>[] };
	map2: { [ index: number ]: BoundedTypesDTO<T, A, B[]>[] };
	map3: { [ index: number ]: BoundedTypesDTO<T, A, B[]>[] };
	map4: Map<BoundedTypesDTO<T, A, B>[], BoundedTypesDTO<T, A, B[]>[]>;
}

export interface BoundedTypesNestedGenericDTO<T extends A, A extends string, B> {
	t: T;
}

export interface GenericDTO<T> {
}
