// Ambient module declarations for CSS imports.
//
// Metro / NativeWind resolve `.css` and `.module.css` at build time, but TypeScript doesn't know
// their shape — these declarations let `tsc` type-check files that import stylesheets.
//   import '@/global.css';                         // side-effect import (global styles)
//   import classes from './x.module.css';          // CSS-module class map

declare module '*.module.css' {
  const classes: { readonly [key: string]: string };
  export default classes;
}

declare module '*.css';
