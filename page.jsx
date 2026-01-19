"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import Modal from "@/components/ui/Modal";
import Icon from "@/components/Icon";
import { getDocuments, saveDocument, deleteDocument } from "@/lib/storage";
import { fileToBase64, generateId } from "@/lib/utils";

export default function Dashboard() {
  const router = useRouter();
  const [documents, setDocuments] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadError, setUploadError] = useState("");

  // Fetch documents on mount
  useEffect(() => {
    const fetchDocs = () => {
      const docs = getDocuments();
      setDocuments(docs);
      setIsLoading(false);
    };

    fetchDocs();
  }, []);

  const handleFileChange = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // Validate image type
    if (!file.type.startsWith("image/")) {
      setUploadError("Please upload a valid image file (JPEG, PNG, WebP).");
      return;
    }

    setIsUploading(true);
    setUploadError("");

    try {
      // Convert to Base64
      const base64Image = await fileToBase64(file);
      
      // Create new document object
      const newDoc = {
        id: generateId(),
        title: file.name.replace(/\.[^/.]+$/, ""), // Remove extension
        image: base64Image,
        fields: [], // Initial empty fields
        // createdAt and updatedAt are handled by saveDocument for new entries
      };

      // Save to localStorage
      const saved = saveDocument(newDoc);
      
      if (saved) {
        // Redirect to editor
        router.push(`/editor/${saved.id}`);
      } else {
        setUploadError("Failed to save document. Your browser storage might be full.");
        setIsUploading(false);
      }
    } catch (error) {
      console.error("Upload error:", error);
      setUploadError("An error occurred while processing the image.");
      setIsUploading(false);
    }
  };

  const handleDelete = (id) => {
    if (window.confirm("Are you sure you want to delete this document? This action cannot be undone.")) {
      const success = deleteDocument(id);
      if (success) {
        setDocuments((prev) => prev.filter((doc) => doc.id !== id));
      } else {
        alert("Failed to delete document.");
      }
    }
  };

  const handleOpenEditor = (id) => {
    router.push(`/editor/${id}`);
  };

  return (
    <div className="space-y-8 animate-in fade-in duration-500">
      {/* Header Section */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-base-200 pb-6">
        <div>
          <h1 className="text-3xl font-bold text-base-content">My Documents</h1>
          <p className="text-base-content/60 mt-1">Manage and edit your scanned templates</p>
        </div>
        <Button 
          onClick={() => setIsModalOpen(true)} 
          className="shadow-lg shadow-primary/20"
        >
          <Icon name="Plus" size={20} className="mr-2" />
          New Document
        </Button>
      </div>

      {/* Content Section */}
      {isLoading ? (
        <div className="flex items-center justify-center h-64">
          <span className="loading loading-spinner loading-lg text-primary"></span>
        </div>
      ) : documents.length === 0 ? (
        /* Empty State */
        <div className="flex flex-col items-center justify-center py-16 px-4 text-center bg-base-200/30 rounded-3xl border-2 border-dashed border-base-300">
          <div className="bg-base-100 p-4 rounded-full shadow-sm mb-4">
            <Icon name="Files" size={48} className="text-base-content/20" />
          </div>
          <h3 className="text-xl font-bold text-base-content mb-2">No documents yet</h3>
          <p className="text-base-content/60 max-w-md mb-8">
            Upload a document scan to get started. You can detect fields automatically or draw them manually.
          </p>
          <Button onClick={() => setIsModalOpen(true)} variant="outline">
            <Icon name="Upload" size={18} className="mr-2" />
            Upload First Document
          </Button>
        </div>
      ) : (
        /* Grid Layout */
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {documents.map((doc) => (
            <Card
              key={doc.id}
              title={doc.title}
              date={doc.updatedAt}
              thumbnail={doc.image}
              onEdit={() => handleOpenEditor(doc.id)}
              onDelete={() => handleDelete(doc.id)}
            />
          ))}
        </div>
      )}

      {/* Upload Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => !isUploading && setIsModalOpen(false)}
        title="Upload New Document"
      >
        <div className="space-y-4">
          <div className="p-8 border-2 border-dashed border-base-300 rounded-xl bg-base-200/30 hover:bg-base-200/50 transition-colors text-center relative group">
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              disabled={isUploading}
              className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10 disabled:cursor-not-allowed"
            />
            
            {isUploading ? (
              <div className="flex flex-col items-center justify-center py-4">
                <span className="loading loading-spinner loading-md text-primary mb-3"></span>
                <p className="text-sm font-medium text-base-content/70">Processing image...</p>
              </div>
            ) : (
              <div className="flex flex-col items-center justify-center py-4">
                <div className="bg-primary/10 p-3 rounded-full mb-3 group-hover:scale-110 transition-transform duration-300">
                  <Icon name="UploadCloud" size={32} className="text-primary" />
                </div>
                <p className="text-base font-semibold text-base-content">Click to upload or drag and drop</p>
                <p className="text-xs text-base-content/50 mt-1">SVG, PNG, JPG or GIF (max. 5MB)</p>
              </div>
            )}
          </div>

          {uploadError && (
            <div className="alert alert-error text-sm py-2 rounded-lg">
              <Icon name="AlertCircle" size={16} />
              <span>{uploadError}</span>
            </div>
          )}

          <div className="flex justify-end gap-2 pt-2">
            <Button 
              variant="ghost" 
              onClick={() => setIsModalOpen(false)}
              disabled={isUploading}
            >
              Cancel
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}