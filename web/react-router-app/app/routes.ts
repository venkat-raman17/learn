import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  index("routes/home.tsx"),
  route("users", "routes/users.tsx"),
  route("users/:id", "routes/users.$id.tsx"),
  route("contact", "routes/contact.tsx"),
] satisfies RouteConfig;
