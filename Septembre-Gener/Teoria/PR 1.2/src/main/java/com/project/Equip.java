package com.project;

/**
 * Un 'record' per emmagatzemar les dades d'un equip d'eSports.
 * És una forma compacta de crear una classe de dades immutable.
 */
public record Equip(boolean actiu, int jugadors, double premis, String nom) {
}