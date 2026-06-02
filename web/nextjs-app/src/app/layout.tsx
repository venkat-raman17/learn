import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";
import "./globals.css";

const geistSans = Geist({ variable: "--font-geist-sans", subsets: ["latin"] });
const geistMono = Geist_Mono({ variable: "--font-geist-mono", subsets: ["latin"] });

export const metadata: Metadata = {
  title: { default: "Next.js 16 — Rendering Patterns", template: "%s | Next.js 16" },
  description: "RSC, SSR, SSG, ISR, Streaming, Server Actions, Micro-frontends",
};

const navLinks = [
  { href: "/", label: "Home (SSG)" },
  { href: "/blog", label: "Blog (CMS+SSG)" },
  { href: "/dashboard", label: "Dashboard (SSR)" },
  { href: "/stream", label: "Stream" },
  { href: "/actions", label: "Actions" },
  { href: "/micro-frontend", label: "Micro-frontend" },
];

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" className={`${geistSans.variable} ${geistMono.variable}`}>
      <body style={{ margin: 0, fontFamily: "var(--font-geist-sans), system-ui, sans-serif", lineHeight: 1.6 }}>
        <nav
          style={{
            borderBottom: "1px solid #e0e0e0",
            padding: "0.75rem 1.5rem",
            display: "flex",
            gap: "1.25rem",
            alignItems: "center",
            flexWrap: "wrap",
          }}
        >
          <span style={{ fontWeight: 700, marginRight: "0.5rem" }}>Next.js 16</span>
          {navLinks.map((l) => (
            <Link
              key={l.href}
              href={l.href}
              style={{ color: "#0066cc", textDecoration: "none", fontSize: "0.9rem", fontWeight: 500 }}
            >
              {l.label}
            </Link>
          ))}
        </nav>
        {children}
      </body>
    </html>
  );
}
