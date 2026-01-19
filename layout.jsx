import "./globals.css";
import Header from "@/components/layout/Header";
import Footer from "@/components/layout/Footer";

export const metadata = {
  title: "DocuFlow - Intelligent Document Editor",
  description: "Transform static document scans into dynamic, editable templates with AI-powered field detection.",
  keywords: ["document", "editor", "template", "pdf", "scan", "ocr", "ai"],
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" data-theme="light">
      <body className="min-h-screen flex flex-col bg-base-100 text-base-content antialiased font-sans selection:bg-primary/20 selection:text-primary">
        <Header />
        <main className="flex-1 w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {children}
        </main>
        <Footer />
      </body>
    </html>
  );
}